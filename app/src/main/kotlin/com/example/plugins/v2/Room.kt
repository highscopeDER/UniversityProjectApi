package com.example.plugins.v2

import kotlinx.serialization.Serializable

@Serializable
data class Room (
    val name: String,
    val label: String,
    val points: List<Pair<Float, Float>>
)
