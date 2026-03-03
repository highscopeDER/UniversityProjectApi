package com.example.plugins.v2.graph

import com.example.plugins.v2.NewDao
import io.ktor.server.application.Application
import kotlinx.coroutines.launch

fun Application.graphHandler(dao: NewDao){

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

        val graph: Graph = links.toMap()

    }

}