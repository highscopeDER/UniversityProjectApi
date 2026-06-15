package com.example.modules.dbModule.models.dbo

import com.example.modules.dbModule.models.dto.Point
import com.example.modules.dbModule.tables.Coord
import org.jetbrains.exposed.v1.core.ResultRow

data class CoordRow(
    val pointName: String,
    val typeDescription: String?,
    val type: Int,
    val x: Float,
    val y: Float
) {

    fun toPoint(): Point {
        return Point(
            this.pointName,
            this.typeDescription,
            this.type,
            this.x,
            this.y
        )
    }

    companion object {
        fun fromResultRow(r: ResultRow): CoordRow {
            return CoordRow(
                r[Coord.pointName],
                r[Coord.typeDesc],
                r[Coord.type],
                r[Coord.x],
                r[Coord.y],
            )
        }
    }
}
