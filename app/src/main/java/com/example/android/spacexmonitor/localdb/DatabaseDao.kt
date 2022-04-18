package com.example.android.spacexmonitor.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {

    // получить строки с индексом от beginRowIndex до endRowIndex (нумерация с нуля)
    @Query("SELECT * FROM launches_table WHERE row_index >= :beginRowIndex AND row_index <= :endRowIndex ORDER BY row_index")
    fun getLaunches(beginRowIndex: Int, endRowIndex: Int): List<DBOneLaunch>

    // вставить строки в таблицу
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaunches(vararg launches: DBOneLaunch)

    // очистить кэш
    @Query("delete from launches_table")
    fun clearTable()
}
