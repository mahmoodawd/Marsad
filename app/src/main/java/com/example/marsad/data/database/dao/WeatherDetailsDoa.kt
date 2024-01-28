package com.example.marsad.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marsad.data.database.entities.WeatherForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDetailsDoa {

    @Query("SELECT * FROM WEATHER_DETAILS")
    fun getAll(): Flow<List<WeatherForecastEntity>>

    @Query("SELECT * FROM WEATHER_DETAILS WHERE coord_lat =:lat AND coord_lon =:lon AND language =:language ORDER BY name DESC LIMIT 1")
    fun getWeatherForecast(lat: Double, lon: Double, language: String): Flow<WeatherForecastEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeatherDetails(
        weatherForecastEntity: WeatherForecastEntity,
    ): Long

    @Delete
    suspend fun clearWeatherDetails(
        weatherForecastEntity: WeatherForecastEntity,
    ): Int
}
