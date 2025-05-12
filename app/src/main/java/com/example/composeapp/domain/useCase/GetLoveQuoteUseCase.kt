package com.example.composeapp.domain.useCase

import com.example.composeapp.domain.repository.LoveQuoteRepository
import javax.inject.Inject

class GetLoveQuoteUseCase @Inject constructor(
    private val loveQuoteRepository: LoveQuoteRepository
) {
    suspend operator fun invoke() = loveQuoteRepository.getLoveQuote()
}