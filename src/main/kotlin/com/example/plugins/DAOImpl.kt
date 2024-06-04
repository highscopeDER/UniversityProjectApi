package com.example.plugins

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DAOImpl : DAO {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun getCoordinatesOfPoints(): Map<String, Pair<Float, Float>> = dbQuery {

        val resultMap: MutableMap<String, Pair<Float, Float>> = mutableMapOf()

        Coordinates
            .selectAll()
            .map {
                resultMap[ it[Coordinates.pointName] ] = Pair(
                    it[Coordinates.x],
                    it[Coordinates.y]
                )
            }

        resultMap
    }

    override suspend fun getDataForAlgorithm() : Map<String, List<String>> = dbQuery {
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

    override suspend fun getClassRoomsList(): Map<String, String> = dbQuery {
        Coordinates
            .selectAll()
            .adjustWhere {
                Coordinates.type eq 1
            }
            .orderBy(Coordinates.typeDesc, order = SortOrder.ASC)
            .map {
                it[Coordinates.typeDesc] to
                it[Coordinates.pointName]
            }
            .distinct()
            .toMap()

    }

}