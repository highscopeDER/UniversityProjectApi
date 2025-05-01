package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

fun Application.connection(config: ApplicationConfig) {

    val url = config.property("storage.jdbcURL").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Database.connect(
        url = url,
        user = user,
        password = password
    )

}

object Connection {

    private const val BASE_URL: String = "jdbc:postgresql://localhost:5432/"

    const val USER: String = "postgres"
    const val DRIVER: String = "org.postgresql.Driver"
    const val PASSWORD: String = "admin"

    const val URL_V1: String = BASE_URL + "MIITNAV"
    const val URL_V2: String = BASE_URL + "MIITNAV_V2"

}



