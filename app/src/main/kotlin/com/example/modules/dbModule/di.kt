package com.example.modules.dbModule

import com.example.modules.dbModule.dao.AdminDaoImpl
import com.example.modules.dbModule.dao.MobileDao
import com.example.modules.dbModule.dao.MobileDaoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dbModule = module {

    singleOf(::MobileDaoImpl) bind MobileDao::class
    singleOf(::MobileService)

    singleOf(::AdminDaoImpl)

    singleOf(::AdminService)

}