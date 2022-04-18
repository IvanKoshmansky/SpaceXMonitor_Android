package com.example.android.spacexmonitor.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// кэш для хранения списка запусков
@Entity(tableName = "launches_table")
data class DBOneLaunch constructor (
    @PrimaryKey
    @ColumnInfo(name = "row_index")  // автогенерация не требуется
    val rowIndex: Long,      // нумерация с нуля, построчная (для работы пейджера)

    @ColumnInfo(name = "has_next")
    val hasNext: Int,        // если == 1, то есть следующая страница (либо в таблице либо на сервере)

    @ColumnInfo(name = "icon_url")
    val iconUrl: String?,    // иконка может отсутствовать

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "cores_flight")
    val coresFlight: Int?,   // может отсутствовать (быть null)

    @ColumnInfo(name = "success")
    val success: Int?,       // может быть null

    @ColumnInfo(name = "date_utc")
    val dateUtc: String,

    @ColumnInfo(name = "id")
    val id: String
)
