package com.example.marsad.data.repositories


import com.example.marsad.data.network.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryInterface {
    suspend fun getWeatherStatus(lat:String, lon:String): Flow<WeatherResponse>
}