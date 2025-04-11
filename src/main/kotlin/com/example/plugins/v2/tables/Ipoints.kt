package com.example.plugins.v2.tables

import org.jetbrains.exposed.sql.Table

object Ipoints : Table() {

    val id = integer("ID Точки интереса")
    val description = varchar("Обозначение точки интереса", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}