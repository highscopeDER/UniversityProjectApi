package com.example.modules.dbModule.models

import com.example.modules.dbModule.models.dbo.RoomRow
import kotlinx.serialization.Serializable

@Serializable
data class Room (
    val name: String,
    val label: String,
    val points: List<Pair<Float, Float>>
) {
    companion object {
        fun fromRoomRowList(rows: List<RoomRow>): List<Room> {
            return rows
                .groupBy { it.pointName }
                .map {
                    Room(
                        name = it.key,
                        label = it.value.first().typeDescription,
                        points = rows.map { it.x to it.y }.toList()
                    )
                }
                .toList()
        }
    }
}
