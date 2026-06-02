package com.example.plugins

import com.example.modules.dbModule.AdminRepositoryImpl
import io.ktor.http.HttpStatusCode
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

    get("/watafaa") {
        call.respond("faaa")
    }

    get("$BASE_GET/auth") {

        val params = call.request.queryParameters
        val login = params.getOrFail<String>("login")
        val password = params.getOrFail<String>("password")

        call.respond(
            repository.auth(login, password) ?: HttpStatusCode.NoContent
        )

    }

    get("$BASE_GET/buildings") {
        call.respond(repository.getBuildingList())
    }

    post("$BASE_POST/{action}/building") {
        when(call.parameters["action"]) {
            "add" -> {}
            "remove" -> {}
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    get("$BASE_GET/rooms") {
        val params = call.request.queryParameters
        val building = params.getOrFail<Char>("building")
        val floor = params["floor"]?.toInt()

        call.respond(repository.getRoomsList(building, floor))

    }

    post("$BASE_POST/{action}/room") {
        when(call.parameters["action"]) {
            "add" -> {}
            "remove" -> {}
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    get("$BASE_GET/points") {

        val params = call.request.queryParameters
        val building = params["building"]?.takeIf { it.length == 1 }?.first()
        val floor = params["floor"]?.toInt()

        call.respond(repository.getPointList(building, floor))
    }

    get("$BASE_GET/graph") {

        val params = call.request.queryParameters
        val building = params.getOrFail<Char>("building")
        val floor = params.getOrFail<Int>("floor")

        call.respond(repository.getGraph(building, floor))
    }

    get("$BASE_GET/floor_map") {

        val params = call.request.queryParameters
        val building = params.getOrFail<Char>("building")
        val floor = params.getOrFail<Int>("floor")

        call.respond(emptyList<Int>())
    }

}