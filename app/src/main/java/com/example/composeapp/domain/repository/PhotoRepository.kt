package com.example.composeapp.domain.repository

import android.net.Uri
import com.example.composeapp.data.local.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun savePhoto(uri: Uri)
    fun getAllPhoto() : Flow<List<PhotoEntity>>
    suspend fun deletePhoto(photo: PhotoEntity)
}