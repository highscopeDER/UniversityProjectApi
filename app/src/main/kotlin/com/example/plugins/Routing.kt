package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {

        get("/check") {
            call.respondText("")
        }

        dbRouting()
        pathFindingRouting()
        adminRouting()
        swagger()

    }
}
