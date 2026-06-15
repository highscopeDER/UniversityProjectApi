package com.example.modules.dbModule

import com.example.modules.dbModule.models.Floor

typealias Coordinates = Map<String, Pair<Float, Float>>
typealias Neighbours = Map<String, List<String>>
typealias Rooms = List<Floor>

const val chars = "_abcdefghijklmn"

val Int.letter: Char
    get() = chars[this]

val Char.num: Int
    get() = chars.indexOf(this)