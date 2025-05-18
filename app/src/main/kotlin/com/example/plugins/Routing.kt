package com.example.plugins

import com.example.plugins.v2.NewDaoImpl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val newDao = NewDaoImpl()

    routing {

        get("/check") {
            call.respondText("")
        }

	get("/verify") {
	    call.respond(newDao.verify())
	}
	

        get("/allClassRooms") {
            call.respond(newDao.classRoomsList())
        }

        get("/data") {
            call.respond(newDao.dataForAlgorithm())
        }

        get("/coordinates") {
            call.respond(newDao.coordinatesOfPoints())
        }

        get("/rooms") {
            call.respond(newDao.rooms())
        }

        get("/roomsCoordinates") {

        }

    }

}
