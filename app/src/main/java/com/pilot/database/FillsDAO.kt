package com.pilot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.Deferred

@Dao
interface FillsDAO {

    @Insert
    fun insert(fill : FillsDTO)

    @Query("SELECT * from data_fills_table order by fillID DESC")
    fun getAllFills() : LiveData<List<FillsDTO>>

    @Query("SELECT * from data_fills_table where fillID = :key")
    fun get(key : Long) : FillsDTO

    @Query("DELETE from data_fills_table where fillID = :key")
    fun deleteThisFill(key : Long)

    @Query("DELETE from data_fills_table")
    fun deleteAll()
}