package com.example.modules.dbModule.tables

import org.jetbrains.exposed.v1.core.Table

object Ipoints : Table() {

    val id = integer("ID Точки интереса")
    val description = varchar("Обозначение точки интереса", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}