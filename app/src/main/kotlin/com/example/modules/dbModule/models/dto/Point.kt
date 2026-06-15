package com.example.modules.dbModule.models.dto

import com.example.modules.dbModule.models.dbo.CoordRow
import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val name: String,
    val label: String?,
    val type: Int,
    val x: Float,
    val y: Float,
) {
    fun toCoordRow(): CoordRow {

        return CoordRow(
            pointName = name,
            typeDescription = label,
            type = type,
            x = x,
            y = y
        )
    }
}
