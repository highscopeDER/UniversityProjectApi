package com.example.plugins.v2

import com.example.plugins.v2.tables.Coord
import com.example.plugins.v2.tables.Neighbour
import com.example.plugins.v2.tables.Rooms
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class NewDaoImpl : NewDao {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

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
    
        val floors: MutableList<Floor> = mutableListOf()
        val rooms: MutableList<Room> = mutableListOf()

        var floor: Int = 1
        var roomLabel: String = "0"
        var roomName = ""
        val points: MutableList<Pair<Float, Float>> = mutableListOf()

        Rooms
            .selectAll()
            .orderBy( Rooms.pointName to SortOrder.ASC, Rooms.pointType to SortOrder.ASC, Rooms.pointPos to SortOrder.ASC)
            .map {

                if (roomLabel != it[Rooms.pointType]) {
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

                if (it[Rooms.pointName][1].digitToInt() != floor) {
                    if (rooms.isNotEmpty()) {
                        floors.add(Floor(floor, rooms.toList()))
                    }
                    rooms.clear()
                    floor = it[Rooms.pointName][1].digitToInt()
                }

            }

        rooms.add(Room(roomName, roomLabel, points.toList()))
        floors.add(Floor(floor, rooms.toList()))
   
        floors.toList()
    }


}
