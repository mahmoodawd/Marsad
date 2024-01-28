package com.example.marsad.domain.datasources

import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.data.network.response.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherDetailsLocalDataSourceInterface {
    fun <T> getAllItems(): Flow<List<T>>
    fun getWeatherForecast(lat: Double, lon: Double, language: String): Flow<WeatherForecastEntity?>

    suspend fun addNewItem(item: OneCallWeatherResponse): Long

    suspend fun updateWeatherDetails(weatherForecastEntity: WeatherForecastEntity): Long

    suspend fun deleteItem(item: OneCallWeatherResponse): Int
}