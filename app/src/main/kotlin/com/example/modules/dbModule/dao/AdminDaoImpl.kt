package com.example.modules.dbModule.dao

import com.example.modules.dbModule.models.User
import com.example.modules.dbModule.models.dbo.CoordRow
import com.example.modules.dbModule.models.dbo.IPointRow
import com.example.modules.dbModule.models.dbo.NeighbourRow
import com.example.modules.dbModule.models.dbo.RoomRow
import com.example.modules.dbModule.tables.Coord
import com.example.modules.dbModule.tables.Ipoints
import com.example.modules.dbModule.tables.Neighbour
import com.example.modules.dbModule.tables.Rooms
import com.example.modules.dbModule.tables.Users
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.Statement
import org.jetbrains.exposed.v1.core.statements.buildStatement
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.statements.toExecutable
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class AdminDaoImpl : AdminDao {

    private suspend fun <T> dbQuery(block: suspend JdbcTransaction.() -> T): T = suspendTransaction { block() }

    suspend fun runInTransaction(st: List<Statement<Any>>): String? {
        return try {
            dbQuery {
                st.forEach { it.toExecutable().execute(this) }
                null
            }
        } catch (e: Exception) {
            e.localizedMessage ?: "Unknown database error occurred"
        }
    }

    private fun ResultRow.toUser(): User {
        return User(
            this[Users.id],
            this[Users.name],
            this[Users.email],
            this[Users.login],
            this[Users.password],
            this[Users.role],
            this[Users.active],
            this[Users.access]
        )
    }

    suspend fun getUsers(): List<User> = dbQuery {
        Users.selectAll().map { it.toUser() }
    }

    suspend fun updateUser(newUser: User) = dbQuery {
        Users.updateReturning(where = { Users.id eq newUser.id}) {
            it[name] = newUser.name
            it[email] = newUser.mail
            it[role] = newUser.role
            it[active] = newUser.active
            it[access] = newUser.access
        }.single().toUser()
    }

    suspend fun insertUser(newUser: User): User = dbQuery {
        Users.insertReturning {
            it[name] = newUser.name
            it[email] = newUser.mail
            it[login] = newUser.login
            it[password] = newUser.password
            it[role] = newUser.role
            it[active] = newUser.active
            it[access] = newUser.access
        }.single().toUser()
    }

    suspend fun deleteUser(id: Int) = dbQuery {
        Users.deleteWhere { Users.id eq id }
    }

    suspend fun getCoordinates(): List<CoordRow> = dbQuery {
        Coord.selectAll().map { CoordRow.fromResultRow(it) }
    }

    fun upsertCoordinateStatement(p: CoordRow) : Statement<Any> =
        buildStatement {
            Coord.upsert {
                it[pointName] = p.pointName
                if(p.typeDescription != null) it[typeDesc] = p.typeDescription
                it[type] = p.type
                it[x] = p.x
                it[y] = p.y
            }
        }

    fun deleteCoordinateStatement(name: String): Statement<Any> = buildStatement {
        Coord.deleteWhere { Coord.pointName eq name }
    }

    suspend fun getNeighbours(): List<NeighbourRow> = dbQuery {
        Neighbour
            .selectAll()
            .orderBy(Neighbour.pointName)
            .map { NeighbourRow.fromResultRow(it) }
    }

    fun insertNeighbourStatement(neighbour: NeighbourRow) = buildStatement {
        Neighbour.insert {
            it[pointName] = neighbour.pointName
            it[neighbourName] = neighbour.neighbour
        }
    }

    fun deleteNeighbourStatement(neighbour: NeighbourRow) = buildStatement {
        Neighbour.deleteWhere {
            (Neighbour.pointName eq neighbour.pointName) and
            (Neighbour.neighbourName eq neighbour.neighbour)
        }
    }

    suspend fun getRooms(): List<RoomRow> = dbQuery {
        Rooms
            .selectAll()
            .orderBy(
                Rooms.pointName to SortOrder.ASC,
                Rooms.pointPos to SortOrder.ASC,
            )
            .map { RoomRow.fromResultRow(it) }
    }

    fun upsertRoomStatement(room: RoomRow) = buildStatement {
        Rooms.upsert {
            it[pointName] = room.pointName
            it[pointType] = room.typeDescription
            it[pointPos] = room.position
            it[x] = room.x
            it[y] = room.y
        }
    }

    fun deleteRoomStatement(room: RoomRow) = buildStatement {
        Rooms.deleteWhere {
            (Rooms.pointName eq room.pointName) and (Rooms.pointPos eq room.position)
        }
    }

    suspend fun getIPointsList(): List<IPointRow> = dbQuery {
         Ipoints.selectAll().map { IPointRow.fromResultRow(it) }
    }




}