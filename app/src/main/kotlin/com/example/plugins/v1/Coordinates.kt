package com.example.plugins.v1

import org.jetbrains.exposed.sql.Table

object Coordinates : Table() {
    val pointId = integer("point_id")
    val pointName = varchar("point_description", 5)
    val type = reference("type_id", PointTypes.id)
    val typeDesc = varchar("type_description", 50)
    val x = float("axis_x")
    val y = float("axis_y")

    override val primaryKey: PrimaryKey = PrimaryKey(arrayOf(pointId, pointName))

}



