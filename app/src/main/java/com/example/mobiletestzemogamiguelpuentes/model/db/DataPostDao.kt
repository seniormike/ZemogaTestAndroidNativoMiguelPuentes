package com.example.mobiletestzemogamiguelpuentes.model.db

import androidx.room.*

@Dao
interface DataPostDao {
    @Query("SELECT * FROM DataPost")
    fun getAll(): List<DataPost>

    @Query("SELECT * FROM DataPost WHERE postId LIKE :postId")
    fun findById(postId: Int): DataPost

    @Insert
    fun insertOne(vararg value: DataPost)

    @Insert
    fun insertAll(users: List<DataPost>)

    @Delete
    fun delete(value: DataPost)

    @Query("DELETE FROM DataPost")
    fun deleteAll()

    @Update
    fun update(vararg value: DataPost)
}