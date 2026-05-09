package com.example.modules.pathFindingModule

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class Node(
    val name: String,
    val x: Float,
    val y: Float
) {
    fun costTo(to: Node): Float {
        return sqrt(
            (to.x - x).pow(2) + (to.y - y).pow(2)
        ) + abs(this.name[1].digitToInt() - to.name[1].digitToInt())*10
    }
}





