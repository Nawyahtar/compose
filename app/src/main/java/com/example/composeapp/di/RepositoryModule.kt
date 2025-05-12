package com.example.composeapp.di

import com.example.composeapp.data.repository.CountdownRepositoryImpl
import com.example.composeapp.data.repository.LoveQuoteRepositoryImpl
import com.example.composeapp.data.repository.PhotoRepositoryImpl
import com.example.composeapp.domain.repository.CountdownRepository
import com.example.composeapp.domain.repository.LoveQuoteRepository
import com.example.composeapp.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        impl: PhotoRepositoryImpl
    ): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindCountdownRepository(
        impl: CountdownRepositoryImpl
    ): CountdownRepository

    @Binds
    @Singleton
    abstract fun bindLoveQuoteRepository(
        impl: LoveQuoteRepositoryImpl
    ): LoveQuoteRepository
}