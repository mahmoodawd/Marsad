package com.example.marsad.data.network

import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherStatus(lat: Double, lon: Double): Response<WeatherDetailsResponse>
    suspend fun getLocationWeather(lat: Double, lon: Double): Response<OpenWeatherMapResponse>
    suspend fun getWeatherAlerts(lat: Double, lon: Double): Response<AlertResponse>
}