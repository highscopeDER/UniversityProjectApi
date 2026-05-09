package com.example.modules.dbModule

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDBConnection(config: ApplicationConfig) {

    val url = config.property("storage.jdbcURL").getString()
    val database = config.property("storage.database").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Database.connect(
        url = url + database,
        user = user,
        password = password
    )

}




