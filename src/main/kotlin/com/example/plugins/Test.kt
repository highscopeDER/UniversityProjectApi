package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.connection() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/project",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "admin"
    )
}



