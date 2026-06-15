package com.example.modules.dbModule.models.dto

import com.example.modules.dbModule.models.dbo.CoordRow
import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val name: String,
    val label: String?,
    val x: Float,
    val y: Float,
) {
    fun toCoordRow(): CoordRow {

        val type: Int = when(label) {
            null -> 0
            "Лестница" -> 2
            "Лифт" -> 3
            "Туалет" -> 4
            "Гардероб" -> 5
            "Автомат с едой" -> 6
            "Автомат с кофе" -> 7
            "Столовая" -> 8
            "Кулер" -> 9
            else -> 1
        }

        return CoordRow(
            pointName = name,
            typeDescription = label,
            type = type,
            x = x,
            y = y
        )
    }
}
