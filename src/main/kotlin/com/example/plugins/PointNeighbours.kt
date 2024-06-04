package com.example.plugins

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class PointNeighbours(
    val pointId: Int,
    val pointName: String,
    val neighbourPointName: String
) {
    companion object {
        fun byResultRow(row: ResultRow) : PointNeighbours {
            return PointNeighbours(
                pointId = row[Neighbour.pointId],
                pointName = row[Neighbour.pointName],
                neighbourPointName = row[Neighbour.neighbourName]
            )
        }
    }
}
