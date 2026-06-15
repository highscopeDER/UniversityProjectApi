package com.example.modules.dbModule.models.requests

import com.example.modules.dbModule.models.dbo.NeighbourRow
import kotlinx.serialization.Serializable

@Serializable
data class LinkRequest(
    val from: String,
    val to: String,
) {
    fun toNeighbourRow(): NeighbourRow {
        return NeighbourRow(
            from,
            to
        )
    }
}
