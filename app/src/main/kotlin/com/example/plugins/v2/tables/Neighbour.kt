package com.example.plugins.v2.tables

import org.jetbrains.exposed.sql.Table

object Neighbour : Table() {
    val pointName = reference("Название точки", Coord.pointName)
    val neighbourName = varchar("Соседние точки", 255)
}