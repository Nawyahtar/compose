package com.example.composeapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Header

interface LoveQuoteApi {

    @GET("love-quote/index.php")
    suspend fun getQuote(
        @Header("x-rapidapi-host") host: String = "love-quotes-of-the-day.p.rapidapi.com",
        @Header("x-rapidapi-key") apiKey: String = "3e567ab15amsh62bd1d91ab4778cp181d96jsn8f2dd58a17a8"
    ): LoveQuoteResponse
}
