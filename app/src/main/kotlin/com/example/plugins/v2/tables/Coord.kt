package com.example.plugins.v2.tables

import org.jetbrains.exposed.sql.Table

object Coord : Table() {
    val pointName = varchar("Название точки", 255)
    val typeDesc = varchar("Точка интереса", 255)
    val type = reference("ID Точки интереса", Ipoints.id)
    val x = float("Координата X")
    val y = float("Координата Y")

    override val primaryKey: Table.PrimaryKey = PrimaryKey(pointName)

}