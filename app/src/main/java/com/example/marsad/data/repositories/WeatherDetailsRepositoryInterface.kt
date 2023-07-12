package com.example.marsad.data.repositories

import com.example.marsad.data.network.WeatherDetailsResponse
import kotlinx.coroutines.flow.Flow

interface WeatherDetailsRepositoryInterface {
    suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        forceUpdate: Boolean = true
    ): Flow<WeatherDetailsResponse>

}