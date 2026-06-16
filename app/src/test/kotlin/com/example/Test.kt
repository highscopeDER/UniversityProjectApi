package com.example

import com.example.modules.dbModule.AdminService
import com.example.modules.dbModule.User
import com.example.modules.pathFindingModule.Node
import org.junit.Test
import kotlin.test.assertEquals

class Test {


    val repo = AdminService()

    @Test
    fun auth() {
        mapOf(
            "admin" to "password" to true,
            "admin" to "not_password" to false,
            "not_admin" to "password" to false,
            "not_admin" to "not_password" to false
        ).forEach { (act, exp) ->
            assertEquals(
                exp,
                repo.auth(act.first, act.second) is User
            )
        }
    }

    @Test
    fun getBuildings() {

        val ans = repo.getBuildingList()
        assertEquals(
            listOf('a', 'h', 'j'),
            ans
        )
    }

    @Test
    fun getGraph(){

        val exp = mapOf(
            Node("h1001", 1f,1f) to listOf(
                Node("h1006s", 1f, 1f),
            ),
            Node("h1006s", 1f,1f) to listOf(
                Node("h1001", 1f, 1f),
                Node("h2001s", 1f, 1f),
            ),
        )

        assertEquals(exp, repo.getGraph('h', 1))

    }



}