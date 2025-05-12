package com.example.composeapp.di

import android.app.Application
import androidx.room.Room
import com.example.composeapp.data.local.AppDatabase
import com.example.composeapp.data.local.modal.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, "compose_app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePhotoDao(db: AppDatabase): PhotoDao {
        return db.photoDao()
    }
}