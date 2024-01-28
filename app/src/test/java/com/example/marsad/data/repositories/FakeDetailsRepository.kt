package com.example.marsad.data.repositories

import com.example.marsad.domain.models.WeatherDetails
import com.example.marsad.domain.repositories.WeatherDetailsRepositoryInterface
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeDetailsRepository : WeatherDetailsRepositoryInterface {

    private val weatherDetailsFlow: MutableSharedFlow<WeatherDetails> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        language: String,
    ): Flow<WeatherDetails?> = weatherDetailsFlow


    fun sendDetails(details: WeatherDetails) {
        weatherDetailsFlow.tryEmit(details)
    }

    override suspend fun syncWeatherDetails(lat: Double, lon: Double, language: String): Boolean =
        true
}
