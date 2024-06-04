package com.example.plugins

import org.jetbrains.exposed.sql.Table

object PointTypes : Table() {

    val id = integer("id")
    val description = varchar("description", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}