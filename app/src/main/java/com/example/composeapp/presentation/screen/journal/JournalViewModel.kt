package com.example.composeapp.presentation.screen.journal

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.useCase.SavePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<JournalUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDate: String? = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH))

    fun onSaveMemoryClick (uri: Uri?){
        if(uri != null) {
            viewModelScope.launch {
                savePhotoUseCase(uri)
                _eventFlow.emit(JournalUiEvent.SaveSuccess)
            }
        }else {
            viewModelScope.launch {
                _eventFlow.emit(JournalUiEvent.ShowToast("Please select a photo first"))
            }
        }
    }

    sealed class JournalUiEvent {
        data class ShowToast(val message: String) : JournalUiEvent()
        object SaveSuccess : JournalUiEvent()
    }
}