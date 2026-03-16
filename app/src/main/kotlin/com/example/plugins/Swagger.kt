package com.example.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureSwagger(){

//    install(CORS) {
//        anyHost()
//        allowHeader(HttpHeaders.ContentType)
//    }
//
//    routing {
//        swaggerUI(path = "swagger") {
//
//        }
//    }

}