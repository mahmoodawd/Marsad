package com.example.marsad.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("lang") lang: String
    ): Response<OneCallResponse>

    @GET("weather")
    suspend fun getLocationWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Response<OpenWeatherMapResponse>
}
