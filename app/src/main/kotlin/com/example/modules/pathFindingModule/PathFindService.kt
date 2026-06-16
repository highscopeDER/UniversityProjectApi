package com.example.modules.pathFindingModule

import com.example.modules.dbModule.MobileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PathFindService(dbRepository: MobileService) {

    private val graph = combine(dbRepository.coordinates, dbRepository.neighbours) { c, n ->
        Graph(c, n)
    }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, Graph.empty())

    fun graphNodes(): List<Node> = graph.value.getNodeList()

    fun buildRoute(start: String, end: String): List<Node>? {
        val startNode = graph.value.findNodeByKey(start)
        val endNode = graph.value.findNodeByKey(end)

        return if(startNode == null || endNode == null) null
        else graph.value.search(startNode, endNode)
    }

}