package com.example.plugins

import com.example.modules.pathFindingModule.PathFindingRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.ktor.ext.get
import kotlin.text.isBlank

fun Route.pathFindingRouting() {

    val repository: PathFindingRepositoryImpl = get()

    get("/searchPath/nodes") {
        call.respond(
            repository.graphNodes()
        )
    }

    get("/searchPath/request") {

        val params = call.request.queryParameters
        val start = params["start"]
        val end = params["end"]
        if ((start == null || end == null) || (start.isBlank() || end.isBlank())) {
            call.respond("not valid or no points")
            return@get
        }

        call.respond(
            repository.buildRoute(start, end)?: "no nodes that matches given points"
        )

    }

}