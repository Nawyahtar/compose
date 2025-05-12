package com.example.composeapp.domain.useCase

import android.net.Uri
import com.example.composeapp.domain.repository.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase@Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(uri: Uri) {
        repository.savePhoto(uri)
    }
}