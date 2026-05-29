package com.example.modules.staticContentModule

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Application.staticSvg() {

    routing {
        staticFiles("/resources", File("svg"))
    }

}