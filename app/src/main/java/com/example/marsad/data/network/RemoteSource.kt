package com.example.marsad.data.network

import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherStatus(lat: Double, lon: Double): Response<OneCallResponse>
    suspend fun getLocationWeather(lat: Double, lon: Double): Response<OpenWeatherMapResponse>

}