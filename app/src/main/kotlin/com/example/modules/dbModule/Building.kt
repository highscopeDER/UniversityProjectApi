package com.example.modules.dbModule

import com.example.modules.dbModule.models.Room
import java.awt.Point

data class Building(
    val id: Int,
    val name: String,
    val floors: List<Floor>,
)

data class Floor(
    val id: Int,
    val name: String,
    val rooms: List<Room_>,
)

data class Room_(
    val id: Int,
    val label: String,
    val graphNode: String,
    val boundingPoints: List<Point>
)


//room points - id, poiId, x, y
// floors - id, buildingId, name +
// buildings - id, name +

// poi - id, graph node, label, typ

// graph nodes - id, floorId, x, y
// graph edges - nodeId1, nodeId2

