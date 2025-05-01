package com.example.plugins.v2

interface NewDao {

    suspend fun coordinatesOfPoints(): Map<String, Pair<Float, Float>>

    suspend fun dataForAlgorithm(): Map<String, List<String>>

    suspend fun classRoomsList() : Map<String, String>

    suspend fun rooms() : List<Floor>

}