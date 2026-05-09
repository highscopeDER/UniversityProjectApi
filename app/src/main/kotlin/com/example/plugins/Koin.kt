package com.example.plugins

import com.example.modules.dbModule.dbModule
import com.example.modules.pathFindingModule.pathFindingModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){

    install(Koin) {
        modules(
            dbModule, pathFindingModule
        )
    }

}