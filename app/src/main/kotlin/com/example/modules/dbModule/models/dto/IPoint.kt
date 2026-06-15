package com.example.modules.dbModule.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class IPoint(
    val id: Int,
    val label: String,
)
