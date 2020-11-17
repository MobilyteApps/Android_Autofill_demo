package com.pilot.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_fills_table")
data class FillsDTO(

    @PrimaryKey(autoGenerate = true)
    var fillID: Long = 0L,

    @ColumnInfo(name = "username")
    var username : String = "",

    @ColumnInfo(name = "password")
    var password : String  = "",

    @ColumnInfo(name = "web_domain")
    var webDomain : String = ""
)