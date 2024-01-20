package com.example.marsad.domain.datasources

import com.example.marsad.data.network.response.CurrentWeatherResponse
import com.example.marsad.data.network.response.OneCallWeatherResponse
import com.example.marsad.data.network.response.WeatherForecastResponse
import retrofit2.Response

interface WeatherDetailsRemoteSourceInterface {
    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String,
    ): WeatherForecastResponse

    suspend fun getCurrentWeather(lat: Double, lon: Double): Response<CurrentWeatherResponse>
    suspend fun getWeatherAlerts(lat: Double, lon: Double): OneCallWeatherResponse
}