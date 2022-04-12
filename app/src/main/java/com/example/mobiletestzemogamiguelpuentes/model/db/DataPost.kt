package com.example.mobiletestzemogamiguelpuentes.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataPost(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var postId: Int = 0,
    var userId: Int = 0,
    var title: String = "",
    var body: String = "",
    var favorite: Boolean = false
)