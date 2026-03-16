package com.example.modules.pathFindingModule

import com.example.modules.dbModule.dao.NewDao
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Application.graphHandler(dao: NewDao){

    val graph: MutableStateFlow<Graph> = MutableStateFlow(emptyMap())

    launch {
        val nodes = dao.coordinatesOfPoints().map {
            it.key to Node(
                name = it.key,
                x = it.value.first,
                y = it.value.second
            )
        }
        val links = dao.dataForAlgorithm().map { link ->
            nodes.first{it.first == link.key}.second to link.value.map { p ->
                nodes.first{ it.first == p }.second
            }
        }

        graph.value = links.toMap()

    }

    routing {

        get("/searchPath/nodes") {
            call.respond(
                graph.value.keys
            )
        }

        get("/searchPath/request") {

            val params = call.request.queryParameters
            val start = params["start"]
            val end = params["end"]
            if ((start == null || end == null) || (start.isBlank() || end.isBlank())) {
                call.respond("not valid points")
                return@get
            }
            graph.collectLatest {



                val startNode = it.findNodeByKey(start)
                val endNode = it.findNodeByKey(end)

                if(startNode == null || endNode == null) {
                    call.respond("no nodes that matches given points")
                    return@collectLatest
                }

                call.respond(
                    search(
                        graph = it,
                        start = startNode,
                        goal = endNode
                    )
                )
            }

        }

    }

}