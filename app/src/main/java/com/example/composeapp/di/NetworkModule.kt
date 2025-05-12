package com.example.composeapp.di

import com.example.composeapp.data.remote.api.LoveQuoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://love-quotes-of-the-day.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideLoveQuoteApi(retrofit: Retrofit): LoveQuoteApi =
        retrofit.create(LoveQuoteApi::class.java)
}