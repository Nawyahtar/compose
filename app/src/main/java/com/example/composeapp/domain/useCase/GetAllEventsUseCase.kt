package com.example.composeapp.domain.useCase

import com.example.composeapp.domain.repository.CountdownRepository
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository
) {
    operator fun invoke() = countdownRepository.getAllEvents()
}