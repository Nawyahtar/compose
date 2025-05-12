package com.example.composeapp.domain.repository

import com.example.composeapp.data.remote.api.LoveQuoteResponse

interface LoveQuoteRepository {
    suspend fun getLoveQuote() : LoveQuoteResponse
}