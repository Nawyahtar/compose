package com.example.composeapp.presentation.screen.countdown

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.remote.CountdownEvent
import com.example.composeapp.domain.useCase.SaveEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import androidx.compose.runtime.State
import com.example.composeapp.domain.useCase.GetAllEventsUseCase
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val saveEventUseCase: SaveEventUseCase,
    private val getAllEventsUseCase: GetAllEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CountdownUiState>(CountdownUiState.Loading)
    val uiState: StateFlow<CountdownUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<CountdownUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _eventName = mutableStateOf("")
    val eventName: State<String> = _eventName

    private val _eventDate = mutableStateOf("")
    val eventDate: State<String> = _eventDate

    init {
        fetchEventList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchEventList() {
        viewModelScope.launch {
            try {
                _uiState.value = CountdownUiState.Loading
                getAllEventsUseCase().collectLatest { result ->
                    val sorted = result.sortedBy { event ->
                        try {
                            LocalDate.parse(event.date)  // assumes format "yyyy-MM-dd"
                        } catch (e: Exception) {
                            LocalDate.MAX  // push invalid dates to the end
                        }
                    }
                    delay(1000)
                    _uiState.value = CountdownUiState.Success(sorted)

                }
            } catch (e: Exception) {
                _uiState.value = CountdownUiState.Error(e.message ?: "Unexpected error")
            }

        }
    }

    fun onAddEventClicked() {
        viewModelScope.launch {
            _eventFlow.emit(CountdownUIEvent.OpenAddEventSheet)
        }
    }

    fun onDismissSheet() {
        viewModelScope.launch {
            _eventFlow.emit(CountdownUIEvent.CloseAddEventSheet)
        }
    }

    fun onRequestDatePicker() {
        viewModelScope.launch {
            _eventFlow.emit(CountdownUIEvent.OpenDatePicker)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDatePicked(millis: Long) {
        val date = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ISO_DATE)
        _eventDate.value = date
    }

    fun onEventNameChanged(name: String) {
        _eventName.value = name
    }

    fun onSave() {
        viewModelScope.launch {
            try {
                val event = CountdownEvent(eventName.value, eventDate.value)
                saveEventUseCase(event)
                _eventName.value = ""
                _eventDate.value = ""
                _eventFlow.emit(CountdownUIEvent.CloseAddEventSheet)
                _eventFlow.emit(CountdownUIEvent.ShowSnackBar("Saved Successfully"))
            } catch (e: Exception) {
                _eventFlow.emit(CountdownUIEvent.ShowSnackBar(e.message.toString()))
            }
        }
    }

    sealed class CountdownUIEvent {
        object OpenAddEventSheet : CountdownUIEvent()
        object CloseAddEventSheet : CountdownUIEvent()
        object OpenDatePicker : CountdownUIEvent()
        data class ShowSnackBar(val message: String) : CountdownUIEvent()
    }

    sealed class CountdownUiState {
        object Loading : CountdownUiState()
        data class Success(val events: List<CountdownEvent>) : CountdownUiState()
        data class Error(val message: String) : CountdownUiState()
    }
}
