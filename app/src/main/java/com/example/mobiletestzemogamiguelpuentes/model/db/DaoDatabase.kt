package com.example.mobiletestzemogamiguelpuentes.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DataPost::class], version = 1, exportSchema = false
)

abstract class DaoDatabase : RoomDatabase() {
    abstract fun dataPostDao(): DataPostDao
}