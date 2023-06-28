package com.example.marsad.data.network

import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherStatus(lat: Double, lon: Double): Response<OneCallResponse>

}