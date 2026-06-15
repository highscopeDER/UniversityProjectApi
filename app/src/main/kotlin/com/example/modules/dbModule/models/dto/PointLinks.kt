package com.example.modules.dbModule.models.dto

import com.example.modules.dbModule.models.dbo.NeighbourRow
import kotlinx.serialization.Serializable

@Serializable
data class PointLinks(
    val pointName: String,
    val links: List<String>
) {
    companion object {

        fun fromNeighbourRowList(rows: List<NeighbourRow>): List<PointLinks> {

            return rows
                .groupBy { it.pointName }
                .map {
                    PointLinks(
                        it.key,
                        it.value.map { it.neighbour }.toList()
                    )
                }
                .toList()
        }

    }
}
