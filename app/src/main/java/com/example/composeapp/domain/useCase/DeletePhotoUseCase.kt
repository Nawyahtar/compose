package com.example.composeapp.domain.useCase

import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.domain.repository.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(photo: PhotoEntity) {
        photoRepository.deletePhoto(photo)

    }
}