package com.example.marsad.data.network.remotesource

import com.example.marsad.core.utils.getCurrentLocale
import com.example.marsad.data.network.RetrofitHelper
import com.example.marsad.domain.datasources.WeatherDetailsRemoteSourceInterface


object WeatherRemoteDataSource : WeatherDetailsRemoteSourceInterface {

    private val retrofitService = RetrofitHelper.retrofitInstance
    private val currentLanguage = getCurrentLocale().language

    override suspend fun getWeatherForecast(lat: Double, lon: Double, language: String) =
        retrofitService.getWeatherForecast(
            lat = lat,
            lon = lon,
            lang = language
        )

    override suspend fun getWeatherAlerts(lat: Double, lon: Double) =
        retrofitService.getWeatherAlerts(lat, lon)

    override suspend fun getCurrentWeather(lat: Double, lon: Double) =
        retrofitService.getCurrentWeather(
            lat = lat,
            lon = lon,
            lang = currentLanguage
        )


}