package com.example.plugins

import com.example.modules.dbModule.DbRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.dbRouting() {
    val repository: DbRepositoryImpl = get()

    get("/allClassRooms") {
        call.respond(repository.roomNamesList.value)
    }

    get("/data") {
        call.respond(repository.neighbours.value)
    }

    get("/coordinates") {
        call.respond(repository.coordinates.value)
    }

    get("/rooms") {
        call.respond(repository.rooms.value)
    }

}