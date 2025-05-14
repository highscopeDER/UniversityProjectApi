package com.example.plugins.v2

import kotlinx.serialization.Serializable

@Serializable
data class Floor(
    val num: FloorsEnum,
    val rooms: List<Room>
)
