package com.example.marsad.data.repositories

import com.example.marsad.data.mappers.toWeatherDetails
import com.example.marsad.data.mappers.toWeatherForecastEntity
import com.example.marsad.domain.datasources.WeatherDetailsLocalDataSourceInterface
import com.example.marsad.domain.datasources.WeatherDetailsRemoteSourceInterface
import com.example.marsad.domain.models.WeatherDetails
import com.example.marsad.domain.repositories.WeatherDetailsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class WeatherDetailsRepository private constructor(
    private val remoteSource: WeatherDetailsRemoteSourceInterface,
    private val localSource: WeatherDetailsLocalDataSourceInterface,
) : WeatherDetailsRepositoryInterface {

    companion object {
        private const val TAG = "WeatherDetailsRepo"
        private var instance: WeatherDetailsRepository? = null
        fun getInstance(
            remoteSource: WeatherDetailsRemoteSourceInterface,
            localSource: WeatherDetailsLocalDataSourceInterface,
        ): WeatherDetailsRepository {
            if (instance == null) {
                instance = WeatherDetailsRepository(remoteSource, localSource)
            }
            return instance as WeatherDetailsRepository
        }
    }

    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        language: String,
    ): Flow<WeatherDetails> {
        val details = localSource.getWeatherForecast(lat, lon, language)
        return details.map {
            if (it == null) syncWeatherDetails(lat, lon, language)
            it!!.toWeatherDetails()
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun syncWeatherDetails(lat: Double, lon: Double, language: String): Boolean {
        return try {
            val weatherForecastResponse = remoteSource.getWeatherForecast(lat, lon, language)
            localSource.updateWeatherDetails(
                weatherForecastResponse.toWeatherForecastEntity(language)
            )
            true
        } catch (e: Exception) {
            false
        }
    }


}