package com.example.modules.dbModule.tables

import org.jetbrains.exposed.v1.core.Table

object Rooms : Table() {

    val pointName = varchar("Название точки", 255)
    val pointType = varchar("Точка интереса", 255)
    val pointPos = integer("Крайние точки")
    val x = float("Координата X")
    val y = float("Координата Y")

}