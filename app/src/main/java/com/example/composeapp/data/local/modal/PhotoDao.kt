package com.example.composeapp.data.local.modal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composeapp.data.local.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: PhotoEntity)

    @Query("SELECT * FROM Gallery ORDER BY createdAt DESC")
    fun getAllPhotos() : Flow<List<PhotoEntity>>

    @Delete
    suspend fun deletePhoto(photo: PhotoEntity)
}