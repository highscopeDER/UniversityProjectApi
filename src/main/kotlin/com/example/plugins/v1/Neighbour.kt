package com.example.plugins.v1

import org.jetbrains.exposed.sql.Table

object Neighbour : Table() {
    val pointId = reference("point_id", Coordinates.pointId)
    val pointName = reference("description", Coordinates.pointName)
    val neighbourName = varchar("neighbour_description", 5)
}