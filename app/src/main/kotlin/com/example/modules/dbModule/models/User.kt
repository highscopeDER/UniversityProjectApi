package com.example.modules.dbModule.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val mail: String,
    val login: String,
    val password: String,
    val role: String,
    val active: Boolean,
    val access: List<Char>
)
