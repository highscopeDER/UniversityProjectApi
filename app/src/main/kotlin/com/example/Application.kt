package com.example

import com.example.plugins.*
import com.example.plugins.v2.NewDaoImpl
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    val dao = NewDaoImpl()

    configureSerialization()
    configureSwagger()
    connection(environment.config)
    configureRouting(dao)
}

