package com.example.marsad.data.repositories


import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.WeatherDetailsResponse
import com.example.marsad.data.network.OpenWeatherMapResponse
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryInterface {

    suspend fun getLocationWeather(lat: Double, lon: Double): Flow<OpenWeatherMapResponse?>
    fun getSavedLocations(): Flow<List<SavedLocation>>
    suspend fun addLocation(savedLocation: SavedLocation): Long
    suspend fun deleteLocation(savedLocation: SavedLocation): Int
    suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        forceUpdate: Boolean = true
    ): Flow<WeatherDetailsResponse>

}