package com.example.modules.dbModule

import com.example.modules.dbModule.dao.MobileDao
import com.example.modules.dbModule.models.Floor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MobileService(val dao: MobileDao) {

    private val _coordinates: MutableStateFlow<Map<String, Pair<Float, Float>>> = MutableStateFlow(emptyMap())
    private val _neighbours: MutableStateFlow<Map<String, List<String>>> = MutableStateFlow(emptyMap())
    private val _rooms: MutableStateFlow<List<Floor>> = MutableStateFlow(emptyList())
    private val _roomNamesList: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())

    init {
        reload()
    }

    val coordinates = _coordinates.asStateFlow()
    val neighbours = _neighbours.asStateFlow()
    val rooms = _rooms.asStateFlow()
    val roomNamesList = _roomNamesList.asStateFlow()

    fun reload() {
        CoroutineScope(Dispatchers.IO).launch {
            _coordinates.update { dao.coordinatesOfPoints() }
            _neighbours.update { dao.dataForAlgorithm() }
            _roomNamesList.update { dao.classRoomsList() }
            _rooms.update { dao.rooms() }
        }

    }
}