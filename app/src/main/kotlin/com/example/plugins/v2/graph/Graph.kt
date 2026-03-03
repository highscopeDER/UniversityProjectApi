package com.example.plugins.v2.graph

import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.math.sqrt

typealias Graph = Map<Node, List<Node>>

data class Node(
    val name: String,
    val x: Float,
    val y: Float
)

fun Node.costTo(to: Node): Float {

    return sqrt(
        (to.x - x).pow(2) + (to.y - y).pow(2)
    )
}

fun search(graph: Graph, start: Node, goal: Node): List<Node> {

    val frontier = PriorityQueue<Pair<Node, Int>>(
        Comparator<Pair<Node, Int>> { p1, p2 ->
            p2.second - p1.second
        }
    )
    val cameFrom: MutableMap<Node, Node?> = mutableMapOf()
    val costList: MutableMap<Node, Float> = mutableMapOf()
    frontier.add(start to 0)
    cameFrom[start] = null
    costList[start] = 0f

    while (!frontier.isEmpty()) {
        val current = frontier.poll().first
        if (current == goal) break

        val neighbours = graph[current] ?: break
        for (next in neighbours) {
            val newCost = costList[current]!! + current.costTo(next)
            if (next !in costList || newCost < costList[next]!!) {
                costList[next] = newCost
                val priority = newCost + next.costTo(goal)
                frontier.add(next to priority.toInt())
                cameFrom[next] = current
            }
        }
    }

    val path = mutableListOf<Node>()
    var current = goal
    path.add(current)
    while (current != start){
        current = cameFrom[current]!!
        path.add(current)
    }

    return path.toList()
}
