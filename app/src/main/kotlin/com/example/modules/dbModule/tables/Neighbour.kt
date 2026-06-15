package com.example.modules.dbModule.tables

import org.jetbrains.exposed.v1.core.Table

object Neighbour : Table() {
    val pointName = reference("Название точки", Coord.pointName)
    val neighbourName = varchar("Соседние точки", 255)
}