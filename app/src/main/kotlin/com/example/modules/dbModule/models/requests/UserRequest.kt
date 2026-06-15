package com.example.modules.dbModule.models.requests

import com.example.modules.dbModule.letter
import com.example.modules.dbModule.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val id: Int,
    val name: String,
    val mail: String,
    val login: String,
    val password: String,
    val role: String,
    val active: Boolean,
    val access: List<Int>
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            mail = mail,
            login = login,
            password = password,
            role = role,
            active = active,
            access = access.map { it.letter }
        )
    }
}
