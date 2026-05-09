package com.example.modules.pathFindingModule

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val pathFindingModule = module {
    singleOf(::PathFindingRepositoryImpl)
}