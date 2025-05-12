package com.example.composeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gallery")
data class PhotoEntity(
    @PrimaryKey
    val photoUri: String,
    val createdAt: Long = System.currentTimeMillis()
)