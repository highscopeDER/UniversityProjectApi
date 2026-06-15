package com.example.modules.dbModule.tables

import org.jetbrains.exposed.v1.core.Table


object Users : Table() {

    val id = integer(name = "id")
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val login = varchar("login", 255)
    val password = varchar("password", 255)
    val role = varchar("role", 255)
    val active = bool("active")
    val access = array<Char>("access")

}