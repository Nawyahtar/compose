package com.example.composeapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.data.local.modal.PhotoDao
import com.example.composeapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val dao: PhotoDao) : PhotoRepository {
    override suspend fun savePhoto(uri: Uri) {
        dao.insertPhoto(PhotoEntity(photoUri = uri.toString()))
    }

    override fun getAllPhoto(): Flow<List<PhotoEntity>> = dao.getAllPhotos()
    override suspend fun deletePhoto(photo: PhotoEntity) {
        dao.deletePhoto(photo)
    }
}