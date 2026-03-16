package com.example.modules.dbModule.tables

import org.jetbrains.exposed.sql.Table

object Neighbour : Table() {
    val pointName = reference("Название точки", Coord.pointName)
    val neighbourName = varchar("Соседние точки", 255)
}