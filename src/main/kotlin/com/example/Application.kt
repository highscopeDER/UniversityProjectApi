package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main(args: Array<String>) = EngineMain.main(args)
// {
//
//    val networkHost = "192.168.43.231"
//    val localHost = "localhost"
//
//
//    embeddedServer(
//        factory = CIO,
//        port = 8080,
//        host = localHost,
//        module = Application::module
//    )
//        .start(wait = true)
//
//
//}

fun Application.module() {
    configureSerialization()
    connection(environment.config)
    configureRouting()
}

