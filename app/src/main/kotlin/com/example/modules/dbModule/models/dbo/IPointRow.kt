package com.example.modules.dbModule.models.dbo

import com.example.modules.dbModule.models.dto.IPoint
import com.example.modules.dbModule.tables.Ipoints
import org.jetbrains.exposed.v1.core.ResultRow

data class IPointRow(
    val id: Int,
    val label: String,
) {

    fun toIPoint(): IPoint {
        return IPoint(id, label)
    }

    companion object {
        fun fromResultRow(row: ResultRow): IPointRow {
            return IPointRow(
                row[Ipoints.id],
                row[Ipoints.description]
            )
        }
    }
}
