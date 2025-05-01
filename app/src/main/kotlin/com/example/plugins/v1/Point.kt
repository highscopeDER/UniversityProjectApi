package com.example.plugins.v1

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val pointId: Int,
    val pointName: String,
    val type: Int,
    val description: String,
    val pointX: Float,
    val pointY: Float
)