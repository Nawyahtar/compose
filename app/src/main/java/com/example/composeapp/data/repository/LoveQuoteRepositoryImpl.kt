package com.example.composeapp.data.repository

import com.example.composeapp.data.remote.api.LoveQuoteApi
import com.example.composeapp.data.remote.api.LoveQuoteResponse
import com.example.composeapp.domain.repository.LoveQuoteRepository
import javax.inject.Inject

class LoveQuoteRepositoryImpl @Inject constructor(
    private val loveQuoteApi: LoveQuoteApi
) : LoveQuoteRepository {
    override suspend fun getLoveQuote(): LoveQuoteResponse {
        return loveQuoteApi.getQuote()
    }

}