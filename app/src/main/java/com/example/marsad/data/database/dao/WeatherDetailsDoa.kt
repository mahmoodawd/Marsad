package com.example.marsad.data.database.dao

import androidx.room.*
import com.example.marsad.data.network.CurrentWeather
import com.example.marsad.data.network.WeatherDetailsResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDetailsDoa {

    @Query("SELECT * FROM weather_details_response")
    fun getAll(): Flow<List<WeatherDetailsResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeatherDetails(weatherDetailsResponse: WeatherDetailsResponse): Long

    @Delete
    suspend fun clearWeatherDetails(weatherDetailsResponse: WeatherDetailsResponse): Int
}
