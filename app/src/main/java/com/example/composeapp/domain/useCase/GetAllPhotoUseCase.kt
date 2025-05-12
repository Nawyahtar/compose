package com.example.composeapp.domain.useCase

import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPhotoUseCase @Inject constructor(private val repository: PhotoRepository) {
     operator fun invoke() : Flow<List<PhotoEntity>>{
        return repository.getAllPhoto()
    }
}