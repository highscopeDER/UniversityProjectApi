package com.example.plugins

import com.example.modules.dbModule.dao.NewDao
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(dao: NewDao) {

    routing {

        get("/check") {
            call.respondText("")
        }

        get("/allClassRooms") {
            call.respond(dao.classRoomsList())
        }

        get("/data") {
            call.respond(dao.dataForAlgorithm())
        }

        get("/coordinates") {
            call.respond(dao.coordinatesOfPoints())
        }

        get("/rooms") {
            call.respond(dao.rooms())
        }

    }

}
