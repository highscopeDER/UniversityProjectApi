package com.example.plugins

interface DAO {


    suspend fun getCoordinatesOfPoints(): Map<String, Pair<Float, Float>>

    suspend fun getDataForAlgorithm(): Map<String, List<String>>

    suspend fun getClassRoomsList() : Map<String, String>

}