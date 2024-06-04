package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val dao = DAOImpl()

    routing {

        get("/allClassRooms") {
            call.respond(dao.getClassRoomsList())
        }

        get("/data") {
            call.respond(dao.getDataForAlgorithm())
        }

        get("/coordinates") {
            call.respond(dao.getCoordinatesOfPoints())
        }

    }
}
