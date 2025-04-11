package com.example.plugins.v2

import kotlinx.serialization.Serializable

@Serializable
data class Floor(
    val num: Int,
    val rooms: List<Room>
)
