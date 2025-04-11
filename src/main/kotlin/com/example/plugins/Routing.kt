package com.example.plugins

import com.example.plugins.v1.DAOImpl
import com.example.plugins.v2.NewDaoImpl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val daoOld = DAOImpl()

    routing {

        get("/allClassRooms") {
            call.respond(daoOld.getClassRoomsList())
        }

        get("/data") {
            call.respond(daoOld.getDataForAlgorithm())
        }

        get("/coordinates") {
            call.respond(daoOld.getCoordinatesOfPoints())
        }

    }

    val newDao = NewDaoImpl()

    routing {

        get("/v2/allClassRooms") {
            call.respond(newDao.classRoomsList())
        }

        get("/v2/data") {
            call.respond(newDao.dataForAlgorithm())
        }

        get("/v2/coordinates") {
            call.respond(newDao.coordinatesOfPoints())
        }

        get("/v2/rooms") {
            call.respond(newDao.rooms())
        }

        get("/v2/roomsCoordinates") {

        }

    }

}
