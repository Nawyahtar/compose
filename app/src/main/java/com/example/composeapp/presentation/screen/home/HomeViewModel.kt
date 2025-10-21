package com.example.composeapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onJournalClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToJournalScreen)
        }
    }

    fun onGalleryClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToGalleryScreen)
        }
    }

    fun onHeartIconClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToTestUiScreen)
        }
    }

    fun onCountDownClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToCountDownScreen)
        }
    }

    fun onLoveQuotesClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToLoveQuotesScreen)
        }
    }

    fun onTapMeClick() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.NavigateToTestUiScreen)
        }
    }

    sealed class HomeUiEvent {
        object NavigateToJournalScreen : HomeUiEvent()
        object ShowToast : HomeUiEvent()
        object NavigateToGalleryScreen : HomeUiEvent()
        object NavigateToCountDownScreen : HomeUiEvent()
        object NavigateToLoveQuotesScreen : HomeUiEvent()
        object NavigateToTestUiScreen: HomeUiEvent()
    }
}

