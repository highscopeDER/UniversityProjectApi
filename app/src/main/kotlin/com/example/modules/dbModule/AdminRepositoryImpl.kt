package com.example.modules.dbModule

import com.example.modules.dbModule.dao.AdminDao
import com.example.modules.dbModule.models.Room
import com.example.modules.pathFindingModule.Node
import kotlinx.serialization.Serializable

class AdminRepositoryImpl {

    fun auth(login: String, password: String): User? {
        return db.users.find {
            it.login == login && it.password == password
        }
    }

    fun getBuildingList(): List<Char> {
        return db.pois
            .map { it.name.first() }
            .distinct()
            .toList()
            .sorted()
    }


    fun addNewBuilding() {

    }

    fun removeBuilding(num: Char) {

    }

    fun getRoomsList(building: Char, floor: Int? = null): List<Room> {
        return db.rooms.filter {
            if (floor != null) it.name[0] == building && it.name[1].digitToInt() == floor
            else it.name[0] == building
        }
    }

    fun addNewRoom(room: Room) {

    }

    fun removeClassroom() {}

    fun addNewFloor() {}

    fun removeFloor() {}

    fun addNewFloorMap() {}

    fun removeFloorMap() {}

    fun getPointList(building: Char? = null, floor: Int? = null): List<Poi> {

        val predicates: MutableList<(Poi) -> Boolean> = mutableListOf()

        if (floor != null) predicates.add { it.name[1].digitToInt() == floor }
        if (building != null) predicates.add { it.name[0] == building }

        return db.pois.filter {
            predicates.all { predicate -> predicate(it) }
        }.toList()
    }

    fun getGraph(building: Char, floor: Int): Map<Node, List<Node>> {

        return db.graph.filter {
            it.key.name.first() == building && it.key.name[1].digitToInt() == floor
        }

    }

    fun getUsers(): List<User> {
        return db.users
    }

    fun removeUser(userId: Int) {
        db.users.removeIf { it.id == userId }
    }

    fun upsertUser(user: User) {
        db.users.find { it.id == user.id }?.let {
            db.users[db.users.indexOf(it)] = user
        } ?: db.users.add(user)
    }

    private object db {

        private val adminUser = User(
            1,
            "irina",
            "ale@mail.ru",
            "admin",
            "password",
            "admin",
            true,
            listOf('a', 'h')

        )

        val users = mutableListOf<User>(adminUser)

        val pois = mutableListOf<Poi>(
            Poi("h1001", null, 1f, 1f),
            Poi("h1006s", null, 1f, 1f),
            Poi("h2001s", null, 1f, 1f),
            Poi("h4001s", null, 1f, 1f),
            Poi("a1001", null, 1f, 1f),
            Poi("a3001", null, 1f, 1f),
            Poi("a3002", null, 1f, 1f),
            Poi("j1001", null, 1f, 1f),
            Poi("j1002", null, 1f, 1f),
            Poi("j2001", null, 1f, 1f),
        )

        val graph = mutableMapOf<Node, List<Node>>(
            Node("h1001", 1f,1f) to listOf(
                Node("h1006s", 1f, 1f),
            ),
            Node("h1006s", 1f,1f) to listOf(
                Node("h1001", 1f, 1f),
                Node("h2001s", 1f, 1f),
            ),
            Node("h2001s", 1f,1f) to listOf(
                Node("h1006s", 1f, 1f),
                Node("h4001s", 1f, 1f),
            ),
            Node("j1001", 1f,1f) to listOf(
                Node("j1002", 1f, 1f),
            ),
            Node("j1002", 1f,1f) to listOf(
                Node("j1001", 1f, 1f),
            ),
        )

        val rooms = mutableListOf<Room>(
            Room(
                "h3013",
                "8305",
                listOf(
                    1f to 1f,
                    1f to 1f,
                    1f to 1f,
                    1f to 1f
                )
            ),
        )


    }


}

@Serializable
data class Poi(
    val name: String,
    val label: String?,
    val x: Float,
    val y: Float
)

@Serializable
data class User(
    val id: Int,
    val name: String,
    val mail: String,
    val login: String,
    val password: String,
    val role: String,
    val active: Boolean,
    val access: List<Char>
)
