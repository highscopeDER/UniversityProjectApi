package com.example

import com.example.modules.dbModule.configureDBConnection
import com.example.modules.staticContentModule.staticSvg
import com.example.plugins.configureKoin
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.plugins.configureCORS
import io.ktor.server.application.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    configureKoin()
    configureSerialization()
    configureCORS()
    configureDBConnection(environment.config)
    configureRouting()
    staticSvg()

}

