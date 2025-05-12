package com.example.composeapp.presentation.screen.loveQuotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.useCase.GetLoveQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoveQuoteViewModel @Inject constructor(
    private val getLoveQuoteUseCase: GetLoveQuoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoveQuoteUiState>(LoveQuoteUiState.Loading)
    val uiState: StateFlow<LoveQuoteUiState> = _uiState.asStateFlow()

    init {
        getQuote()
    }

    fun getQuote() {
        _uiState.value = LoveQuoteUiState.Loading
        viewModelScope.launch {
            try {
                val response = getLoveQuoteUseCase()
                _uiState.value = LoveQuoteUiState.Success(
                    quote = stripHtmlTags(response.quote),
                    author = extractAuthor(response.quote)
                )
            } catch (e: Exception) {
                _uiState.value = LoveQuoteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun stripHtmlTags(html: String): String {
        return html.replace(Regex("<.*?>"), "").trim()
    }

    private fun extractAuthor(html: String): String {
        return Regex("<small>(.*?)</small>").find(html)?.groupValues?.get(1)?.trim() ?: "Unknown"
    }

    sealed class LoveQuoteUiState {
        object Loading : LoveQuoteUiState()
        data class Success(val quote: String, val author: String) : LoveQuoteUiState()
        data class Error(val message: String) : LoveQuoteUiState()
    }
}
