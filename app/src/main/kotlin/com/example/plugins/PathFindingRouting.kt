package com.example.plugins

import com.example.modules.pathFindingModule.PathFindService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.get
import kotlin.text.isBlank

fun Route.pathFindingRouting() {

    val repository: PathFindService = get()

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