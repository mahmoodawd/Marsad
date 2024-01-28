package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.WeatherDetailsDoa
import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.data.network.response.OneCallWeatherResponse
import com.example.marsad.domain.datasources.WeatherDetailsLocalDataSourceInterface
import kotlinx.coroutines.flow.Flow

class WeatherDetailsLocalDataSource(context: Context) :
    WeatherDetailsLocalDataSourceInterface {

    private val weatherDetailsDoa: WeatherDetailsDoa by lazy {
        AppDatabase.getInstance(context).getWeatherDetailsDao()
    }


    override fun <T> getAllItems(): Flow<List<T>> =
        weatherDetailsDoa.getAll() as Flow<List<T>>


    override fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String,
    ): Flow<WeatherForecastEntity?> =
        weatherDetailsDoa.getWeatherForecast(lat, lon, language)

    override suspend fun addNewItem(item: OneCallWeatherResponse): Long =
        1

    override suspend fun updateWeatherDetails(weatherForecastEntity: WeatherForecastEntity) =
        weatherDetailsDoa.updateWeatherDetails(weatherForecastEntity)

    override suspend fun deleteItem(item: OneCallWeatherResponse) =
        1

}