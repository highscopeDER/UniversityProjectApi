package com.example.modules.dbModule.models

import kotlinx.serialization.Serializable

@Serializable
data class Floor(
    val num: Int,
    val rooms: List<Room>
)
