package com.example.modules.dbModule

import com.example.modules.dbModule.dao.AdminDaoImpl
import com.example.modules.dbModule.models.Room
import com.example.modules.dbModule.models.User
import com.example.modules.dbModule.models.dto.Point
import com.example.modules.dbModule.models.dto.PointLinks
import com.example.modules.dbModule.models.requests.LinkRequest
import com.example.modules.dbModule.models.requests.PointInsertRequest
import com.example.modules.dbModule.models.requests.UserRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.buildSerialDescriptor
import org.jetbrains.exposed.v1.core.statements.Statement

class AdminRepositoryImpl(val adminDao: AdminDaoImpl) {

    private val points: MutableStateFlow<List<Point>> = MutableStateFlow(emptyList())
    private val links: MutableStateFlow<List<PointLinks>> = MutableStateFlow(emptyList())
    private val rooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())

    private val operationStore: MutableSharedFlow<Operation> = MutableSharedFlow()
    private val operationStatements: MutableList<Statement<Any>> = mutableListOf()

    init {
        CoroutineScope(Dispatchers.IO).launch {

            launch { reload() }

            operationStore.collect { operation ->
                operationStatements.add(operation.statement)
                when(operation) {
                    is Operation.Points.Cascade -> {
                        points.update { operation.opPoint(it) }
                        links.update { operation.opLink(it) }
                        rooms.update { operation.opRoom(it) }
                    }

                    is Operation.Points -> {
                        points.update { operation.opPoint(it) }
                    }

                    is Operation.PointsLinks -> {
                        links.update { operation.opLink(it) }
                    }

                }
            }
        }
    }

    suspend fun reload() {
        points.update { adminDao.getCoordinates().map { it.toPoint() }.toList() }
        links.update { PointLinks.fromNeighbourRowList(adminDao.getNeighbours()) }
        rooms.update { Room.fromRoomRowList(adminDao.getRooms()) }
    }

    suspend fun auth(login: String, password: String): User? {
        return adminDao.getUsers().find { it.login == login && it.password == password }
    }

    suspend fun getUsers(): List<UserRequest> {
        return adminDao.getUsers().map { it.toUserRequest() }
    }

    suspend fun upsertUser(newUser: UserRequest) {
        if (newUser.id == -1) adminDao.insertUser(newUser.toUser())
        else adminDao.updateUser(newUser.toUser())
    }

    suspend fun deleteUser(user: UserRequest) {
        adminDao.deleteUser(user.id)
    }

    fun getBuildingList(): List<Int> {
        return points.value
            .map { it.name.first().num }
            .distinct()
            .sorted()
    }

    fun getFloorList(building: Int): List<Int> {
        return points.value
            .filter { it.name.first().num == building }
            .map { it.name[1].digitToInt() }
            .distinct()
            .sorted()
    }

    suspend fun applyChanges(): String? {
        val res = adminDao.runInTransaction(operationStatements.toList())
        if (res == null) {
            operationStatements.clear()
            reload()
        }
        return res
    }

    fun getPointList(building: Int?, floor: Int?): List<Point> {

        val predicates: MutableList<(Point) -> Boolean> = mutableListOf()
        if (floor != null) predicates.add { it.name[1].digitToInt() == floor }
        if (building != null) predicates.add { it.name.first() == building.letter }

        return points.value.filter {
            predicates.all { predicate -> predicate(it) }
        }
    }

    suspend fun insertPoint(request: PointInsertRequest) {

        val num = points.value.count {
            it.name.startsWith("${request.building.letter}${request.floor}")
        }.inc().toString().padStart(3, '0')

        val typeChar: String? = when(request.label) {
            "Лестница" -> "s"
            "Лифт" -> "l"
            "Туалет" -> "wc"
            else -> null
        }

        val point = Point(
            name = "${request.building.letter}${request.floor}$num${typeChar.orEmpty()}",
            label = request.label,
            x = request.x,
            y = request.y,
        )

        operationStore.emit(
            Operation.Points.Upsert(
                point,
            adminDao.upsertCoordinateStatement(point.toCoordRow())
            )
        )
    }

    suspend fun updatePoint(point: Point) {
        operationStore.emit(
            Operation.Points.Upsert(
                point,
                adminDao.upsertCoordinateStatement(point.toCoordRow())
            )
        )
    }

    suspend fun deletePoint(point: Point) {
        operationStore.emit(
            Operation.Points.Cascade.Delete(
                point,
                adminDao.deleteCoordinateStatement(point.name)
            )
        )
    }

    fun getLinkList(building: Int?, floor: Int?): List<PointLinks> {
        val predicates: MutableList<(String) -> Boolean> = mutableListOf()
        if (floor != null) predicates.add { it[1].digitToInt() == floor }
        if (building != null) predicates.add { it.first() == building.letter }

        return links.value.filter {
            predicates.all { predicate -> predicate(it.pointName) }
        }
    }

    suspend fun insertLink(link: LinkRequest) {
        operationStore.emit(
            Operation.PointsLinks.Insert(
                link,
                adminDao.insertNeighbourStatement(link.toNeighbourRow())
            )
        )
    }

    suspend fun deleteLink(link: LinkRequest) {
        operationStore.emit(
            Operation.PointsLinks.Delete(
                link,
                adminDao.deleteNeighbourStatement(link.toNeighbourRow())
            )
        )
    }

    sealed interface Operation{
        val statement: Statement<Any>

        sealed interface Points : Operation {
            val item: Point

            fun opPoint(
                list: List<Point>,
            ): List<Point>

            data class Upsert(
                override val item: Point,
                override val statement: Statement<Any>
            ) : Operation.Points {
                override fun opPoint(
                    list: List<Point>,
                ): List<Point> {
                    return if(list.any { it.name == item.name })
                        list.map { if(it.name == item.name) it else item }.toList()
                    else list.plus(item)
                }
            }


            sealed interface Cascade: Points {
                fun opLink(list: List<PointLinks>): List<PointLinks>
                fun opRoom(list: List<Room>): List<Room>

                data class Delete(
                    override val item: Point,
                    override val statement: Statement<Any>
                ): Cascade {
                    override fun opLink(
                        list: List<PointLinks>,
                    ): List<PointLinks> {
                        return list
                            .filterNot { it.pointName == item.name }
                            .map {
                                it.copy(
                                    links = it.links.filterNot { link -> link == item.name }
                                )
                            }
                    }

                    override fun opRoom(
                        list: List<Room>,
                    ): List<Room> {
                        return list.filterNot { it.name == item.name }
                    }

                    override fun opPoint(
                        list: List<Point>,
                    ): List<Point> {
                        return list.minus(item)
                    }

                }

            }


        }

        sealed interface PointsLinks : Operation {

            val link: LinkRequest
            fun opLink(list: List<PointLinks>): List<PointLinks>

            data class Insert(
                override val link: LinkRequest,
                override val statement: Statement<Any>
            ) : PointsLinks {
                override fun opLink(list: List<PointLinks>): List<PointLinks> {
                    val c = list.find { it.pointName == link.from } == null
                    return if (c) {
                        list
                            .plus(PointLinks(link.from, listOf(link.to)))
                            .map {
                                if(it.pointName == link.to) it.copy(links = it.links.plus(link.from))
                                else it
                            }
                    } else list.map {
                        when(it.pointName) {
                            link.from -> it.copy(links = it.links.plus(link.to))
                            link.to -> it.copy(links = it.links.plus(link.from))
                            else -> it
                        }
                    }
                }

            }

            data class Delete(
                override val link: LinkRequest,
                override val statement: Statement<Any>
            ) : PointsLinks {
                override fun opLink(list: List<PointLinks>): List<PointLinks> {
                    val c = list.count { it.links.contains(link.from) }
                    return if (c < 2) {
                        list
                            .filterNot { it.pointName == link.from  }
                            .map {
                                if(it.links.contains(link.from)) it.copy(links = it.links.minus(link.from))
                                else it
                            }
                    } else list.map {
                        when(it.pointName) {
                            link.from -> it.copy(links = it.links.minus(link.to))
                            link.to -> it.copy(links = it.links.minus(link.from))
                            else -> it
                        }
                    }
                }
            }


        }

    }
    
}

