package com.example.composeapp.domain.repository

import com.example.composeapp.data.remote.CountdownEvent
import kotlinx.coroutines.flow.Flow

interface CountdownRepository  {
    suspend fun addEvent(event: CountdownEvent)
    fun getAllEvents() : Flow<List<CountdownEvent>>
}