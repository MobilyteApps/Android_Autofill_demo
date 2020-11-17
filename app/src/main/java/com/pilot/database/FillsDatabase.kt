package com.pilot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FillsDTO::class], version = 1, exportSchema = false)
abstract class FillsDatabase : RoomDatabase() {

    abstract val fillsDatabaseDao: FillsDAO

    companion object {

        @Volatile
        private var INSTANCE: FillsDatabase? = null

        fun getInstance(context: Context) : FillsDatabase{

            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FillsDatabase::class.java,
                        "notes_history_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}