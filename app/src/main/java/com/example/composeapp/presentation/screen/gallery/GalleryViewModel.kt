package com.example.composeapp.presentation.screen.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.domain.useCase.DeletePhotoUseCase
import com.example.composeapp.domain.useCase.GetAllPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllPhotoUseCase: GetAllPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)
    val uiState: StateFlow<GalleryUIState> = _uiState


    private val _confirmDeleteEvent = MutableSharedFlow<GalleryUIEvent>()
    val confirmDeleteEvent = _confirmDeleteEvent.asSharedFlow()

    init {
        loadPhotos()
    }

    fun requestDelete(photo: PhotoEntity) {
        viewModelScope.launch {
            _confirmDeleteEvent.emit(GalleryUIEvent.ShowDeleteAlert(photo))
        }
    }

    fun deletePhoto(photo: PhotoEntity) {
        viewModelScope.launch {
            deletePhotoUseCase(photo)
        }

    }

    private fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = GalleryUIState.Loading
            try {
                getAllPhotoUseCase().collectLatest { photoList ->
                    delay(1000)
                    _uiState.value = GalleryUIState.Success(photoList)
                }
            } catch (e: Exception) {
                _uiState.value = GalleryUIState.Error(e.message ?: "Unknown error")
            }

        }
    }

    sealed class GalleryUIEvent {
        data class ShowDeleteAlert(val photo: PhotoEntity) : GalleryUIEvent()
    }

    sealed class GalleryUIState {
        object Loading : GalleryUIState()
        data class Success(val photos: List<PhotoEntity>) : GalleryUIState()
        data class Error(val message: String) : GalleryUIState()
    }

}