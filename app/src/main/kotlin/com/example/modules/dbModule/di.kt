package com.example.modules.dbModule

import com.example.modules.dbModule.dao.NewDao
import com.example.modules.dbModule.dao.NewDaoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dbModule = module {

    singleOf(::NewDaoImpl) bind NewDao::class
    singleOf(::DbRepositoryImpl)

}