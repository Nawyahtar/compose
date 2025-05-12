package com.example.composeapp.domain.useCase

import com.example.composeapp.data.remote.CountdownEvent
import com.example.composeapp.domain.repository.CountdownRepository
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository
) {
    suspend operator fun invoke(event: CountdownEvent) {
        countdownRepository.addEvent(event)
    }
}