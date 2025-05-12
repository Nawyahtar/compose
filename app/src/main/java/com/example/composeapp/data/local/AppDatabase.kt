package com.example.composeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.data.local.modal.PhotoDao

@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}