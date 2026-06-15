package com.example.modules.dbModule.models.requests

import com.example.modules.dbModule.models.dto.Point
import kotlinx.serialization.Serializable

@Serializable
data class PointInsertRequest(
    val building: Int,
    val floor: Int,
    val label: String?,
    val type: Int,
    val x: Float,
    val y: Float,
)