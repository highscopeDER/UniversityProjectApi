package com.example.plugins.v2

import com.example.plugins.v2.tables.Coord
import com.example.plugins.v2.tables.Neighbour
import com.example.plugins.v2.tables.Rooms
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class NewDaoImpl : NewDao {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun verify(): List<String> = dbQuery {
        val allPoints = Coord.selectAll().map { it[Coord.pointName] }

        Neighbour.selectAll().map {
            if (!allPoints.contains(it[Neighbour.neighbourName]))
                it[Neighbour.neighbourName]
            else
                ""
        }.distinct()
    }

    override suspend fun coordinatesOfPoints(): Map<String, Pair<Float, Float>> = dbQuery {

        val resultMap: MutableMap<String, Pair<Float, Float>> = mutableMapOf()

        Coord
            .selectAll()
            .map {
                resultMap[ it[Coord.pointName] ] = Pair(
                    it[Coord.x],
                    it[Coord.y]
                )
            }

        resultMap
    }


    override suspend fun dataForAlgorithm(): Map<String, List<String>> = dbQuery {
        var lastPoint: String? = null
        val pointsList: MutableList<String> = mutableListOf()
        val neighboursList: MutableList<String> = mutableListOf()
        val listedNeighbours: MutableList<List<String>> = mutableListOf()
        Neighbour
            .selectAll()
            .orderBy(Neighbour.pointName)
            .filter { it[Neighbour.neighbourName].last() != 'l' || it[Neighbour.pointName].last() != 'l' }
            .map {
                val pName = it[Neighbour.pointName]
                val nName = it[Neighbour.neighbourName]
                if (lastPoint == null) {
                    lastPoint = pName

                } else {
                    if (lastPoint != pName) {
                        listedNeighbours.add(neighboursList.toList())
                        neighboursList.clear()
                        pointsList.add(lastPoint!!)
                        lastPoint = pName
                    }
                }
                neighboursList.add(nName)
            }

        pointsList.add(lastPoint!!)
        listedNeighbours.add(neighboursList)

        val resultingMap: MutableMap<String, List<String>> = mutableMapOf()

        pointsList.forEachIndexed { index, s ->
            resultingMap[s] = listedNeighbours[index]
        }

        resultingMap
    }

    override suspend fun classRoomsList(): Map<String, String> = dbQuery {
        Coord
            .selectAll()
            .adjustWhere {
                Coord.type neq 0
            }
            .orderBy(Coord.typeDesc, order = SortOrder.ASC)
            .map {
                it[Coord.pointName] to
                        it[Coord.typeDesc]
            }
            .distinct()
            .toMap()

    }

    override suspend fun rooms(): List<Floor> = dbQuery {

        //val result = mutableMapOf<String, List<Pair<Float, Float>>>()

        val floors: MutableList<Floor> = mutableListOf()
        val rooms: MutableList<Room> = mutableListOf()

        var floor: FloorsEnum = FloorsEnum.A0
        var roomLabel: String = "0"
        var roomName = ""
        val points: MutableList<Pair<Float, Float>> = mutableListOf()

        Rooms
            .selectAll()
            .orderBy( Rooms.pointName to SortOrder.ASC, Rooms.pointType to SortOrder.ASC, Rooms.pointPos to SortOrder.ASC)
            .map {

                if (roomName != it[Rooms.pointName]) {
                    if (points.isNotEmpty()) {
                        //result[room] = points.toList()
                        rooms.add(
                            Room(roomName, roomLabel, points.toList())
                        )
                    }
                    points.clear()
                    roomLabel = it[Rooms.pointType]
                    roomName = it[Rooms.pointName]
                }
                points.add(it[Rooms.x] to it[Rooms.y])

                if (it[Rooms.pointName].floor != floor) {
                    if (rooms.isNotEmpty()) {
                        floors.add(Floor(floor, rooms.toList()))
                    }
                    rooms.clear()
                    floor = it[Rooms.pointName].floor
                }

            }

        rooms.add(Room(roomName, roomLabel, points.toList()))
        floors.add(Floor(floor, rooms.toList()))
        //result[room] = points.toList()

        floors
    }


    private val String.floor: FloorsEnum get() = when(this.first()) {
        'a' -> when(this[1].digitToInt()) {
            0 -> FloorsEnum.A0
            1 -> FloorsEnum.A1
            2 -> FloorsEnum.A2
            3 -> FloorsEnum.A3
            4 -> FloorsEnum.A4
            5 -> FloorsEnum.A5
            6 -> FloorsEnum.A6
            else -> FloorsEnum.A0
        }

        'h' -> when(this[1].digitToInt()) {
            1 -> FloorsEnum.H1
            2 -> FloorsEnum.H2
            3 -> FloorsEnum.H3
            4 -> FloorsEnum.H4
            5 -> FloorsEnum.H5
            else -> FloorsEnum.H1
        }

        'j' -> when(this[1].digitToInt()) {
            1 -> FloorsEnum.J1
            2 -> FloorsEnum.J2
            3 -> FloorsEnum.J3
            4 -> FloorsEnum.J4
            else -> FloorsEnum.J1
        }

        else -> FloorsEnum.A0
    }

}