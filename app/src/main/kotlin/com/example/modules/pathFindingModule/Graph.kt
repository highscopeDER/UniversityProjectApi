package com.example.modules.pathFindingModule

import java.util.PriorityQueue

class Graph(
    coordinates:  Map<String, Pair<Float, Float>>,
    neighbours:  Map<String, List<String>>
) {
    private val graph: Map<Node, List<Node>>

    init {
        val nodes = coordinates.map {
            it.key to Node(
                name = it.key,
                x = it.value.first,
                y = it.value.second
            )
        }
        val links = neighbours.map { link ->
            nodes.first{it.first == link.key}.second to link.value.map { p ->
                nodes.first{ it.first == p }.second
            }
        }

        graph = links.toMap()
    }

    fun findNodeByKey(key: String): Node? {
        return graph.keys.find { it.name == key }
    }

    fun getNodeList(): List<Node> = graph.keys.toList()

    fun search(start: Node, goal: Node): List<Node> {

        val frontier = PriorityQueue<Pair<Node, Int>>(
            Comparator<Pair<Node, Int>> { p1, p2 ->
                p1.second - p2.second
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

    companion object {
        fun empty(): Graph = Graph(emptyMap(), emptyMap())
    }


}