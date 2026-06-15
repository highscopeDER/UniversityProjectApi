package com.example.plugins

import com.example.modules.dbModule.AdminRepositoryImpl
import com.example.modules.dbModule.models.User
import com.example.modules.dbModule.models.dto.Point
import com.example.modules.dbModule.models.requests.LinkRequest
import com.example.modules.dbModule.models.requests.PointInsertRequest
import com.example.modules.dbModule.models.requests.UserRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.param
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.get
import kotlin.collections.emptyList

fun Route.adminRouting() {

    val repository: AdminRepositoryImpl = get()

    val BASE = "/admin"
    val BASE_GET = "$BASE/get"
    val BASE_POST = "$BASE/post"

    get("$BASE_GET/auth") {

        val params = call.request.queryParameters
        val login = params.getOrFail<String>("login")
        val password = params.getOrFail<String>("password")

        call.respond(
            repository.auth(login, password) ?: HttpStatusCode.NoContent
        )

    }

    get("$BASE_GET/users") {
        call.respond(repository.getUsers())
    }


    post("$BASE_POST/{action}/user") {
        val user = call.receive<UserRequest>()

        when(call.parameters["action"]) {
            "add" -> {repository.upsertUser(user)}
            "remove" -> {repository.deleteUser(user)}
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    get("$BASE_GET/buildings") {
        call.respond(repository.getBuildingList())
    }

    get("$BASE_GET/floors") {
        val params = call.request.queryParameters
        val building = params.getOrFail<Int>("building")

        call.respond(repository.getFloorList(building))
    }

    get("$BASE_GET/apply") {
        call.respond(
            repository.applyChanges() ?: HttpStatusCode.OK
        )
    }

    get("$BASE_GET/points") {

        val params = call.request.queryParameters
        val building = params["building"]?.toInt()
        val floor = params["floor"]?.toInt()

        call.respond(repository.getPointList(building, floor))
    }

    post("$BASE_POST/{action}/point") {
        when(call.parameters["action"]) {
            "add" -> {
                val point = call.receive<PointInsertRequest>()
                repository.insertPoint(point)
            }
            "update" -> {
                val point = call.receive<Point>()
                repository.updatePoint(point)
            }
            "remove" -> {
                val point = call.receive<Point>()
                repository.deletePoint(point)
            }
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    get("$BASE_GET/links") {

        val params = call.request.queryParameters
        val building = params["building"]?.toInt()
        val floor = params["floor"]?.toInt()

        call.respond(repository.getLinkList(building, floor))
    }

    post("$BASE_POST/{action}/link") {
        val link = call.receive<LinkRequest>()
        when(call.parameters["action"]) {
            "add" -> {
                repository.insertLink(link)
            }
            "remove" -> {
                repository.deleteLink(link)
            }
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

}