package com.example.modules.dbModule.models.dbo

import com.example.modules.dbModule.tables.Rooms
import org.jetbrains.exposed.v1.core.ResultRow

data class RoomRow(
    val pointName: String,
    val typeDescription: String,
    val position: Int,
    val x: Float,
    val y: Float
) {
    companion object {
        fun fromResultRow(row: ResultRow): RoomRow {
            return RoomRow(
                row[Rooms.pointName],
                row[Rooms.pointType],
                row[Rooms.pointPos],
                row[Rooms.x],
                row[Rooms.y]
            )
        }
    }
}
