package com.example.modules.dbModule.models.dbo

import com.example.modules.dbModule.tables.Neighbour
import org.jetbrains.exposed.v1.core.ResultRow

data class NeighbourRow(
    val pointName: String,
    val neighbour: String
) {
    companion object {
        fun fromResultRow(r: ResultRow): NeighbourRow {
            return NeighbourRow(
                r[Neighbour.pointName],
                r[Neighbour.neighbourName],
            )
        }
    }
}
