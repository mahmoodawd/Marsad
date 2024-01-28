package com.example.marsad.data.network

import com.example.marsad.data.network.response.CurrentWeatherResponse
import com.example.marsad.data.network.response.OneCallWeatherResponse
import com.example.marsad.data.network.response.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "d74e0e363a439e102e4a72c39c09de0b"

interface ApiService {

    @GET("onecall?appid=$API_KEY")
    /**
     * Requires paid subscription to open weather
     * One Call API which gives daily, hourly, and minutely weather data
     * in addition to government weather alerts.
     */
    suspend fun getOneCallWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String,
        @Query("lang") lang: String,
    ): Response<OneCallWeatherResponse>

    @GET("onecall?appid=$API_KEY&exclude=current,minutely,hourly,daily")
    /**
     * Requires paid subscription to open weather
     * One Call API which gives access to weather alerts.
     */
    suspend fun getWeatherAlerts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): OneCallWeatherResponse

    @GET("forecast?appid=$API_KEY")
    /**
     * Get weather forecast for 5 days with data every 3 hours.
     */
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String,
    ): WeatherForecastResponse

    @GET("weather?appid=$API_KEY")
    /**
     * Get current weather for a specified geographical location
     */
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String,
    ): Response<CurrentWeatherResponse>
}
