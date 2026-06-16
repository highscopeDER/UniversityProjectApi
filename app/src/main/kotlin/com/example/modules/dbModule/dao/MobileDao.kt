package com.example.modules.dbModule.dao

import com.example.modules.dbModule.models.Floor

interface MobileDao {

    suspend fun coordinatesOfPoints(): Map<String, Pair<Float, Float>>

    suspend fun dataForAlgorithm(): Map<String, List<String>>

    suspend fun classRoomsList() : Map<String, String>

    suspend fun rooms() : List<Floor>

}