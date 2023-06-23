package com.example.marsad.data.network

import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherStatus(lat: String, lon: String): Response<WeatherResponse>

}