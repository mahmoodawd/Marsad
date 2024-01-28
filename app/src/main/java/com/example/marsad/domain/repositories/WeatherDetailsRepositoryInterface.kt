package com.example.marsad.domain.repositories

import com.example.marsad.domain.models.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherDetailsRepositoryInterface {
    suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        language: String,
    ): Flow<WeatherDetails?>

    suspend fun syncWeatherDetails(lat: Double, lon: Double, language: String): Boolean
}