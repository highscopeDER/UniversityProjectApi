package com.example

import com.example.plugins.*
import com.example.modules.dbModule.dao.NewDaoImpl
import com.example.modules.dbModule.connection
import com.example.modules.pathFindingModule.graphHandler
import io.ktor.server.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    val dao = NewDaoImpl()

    configureSerialization()
    //configureSwagger()
    connection(environment.config)
    configureRouting(dao)
    graphHandler(dao)
}

