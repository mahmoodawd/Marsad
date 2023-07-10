package com.example.marsad.data.repositories

import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.network.OpenWeatherMapResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakeLocationRepository : LocationRepositoryInterface {
    private val flow = MutableSharedFlow<OpenWeatherMapResponse>()
     var addOrDeleteStatus = 0

    override suspend fun getWeatherDetails(lat: Double, lon: Double): Flow<OneCallResponse> {
        TODO("Not yet implemented")
    }

    suspend fun emit(value: OpenWeatherMapResponse) = flow.emit(value)
    override suspend fun getLocationWeather(
        lat: Double,
        lon: Double
    ): Flow<OpenWeatherMapResponse?> = flow

    override fun getSavedLocations(): Flow<List<SavedLocation>> = flow { emit(savedLocations) }

    override suspend fun addLocation(savedLocation: SavedLocation): Long {
        savedLocations.add(savedLocation)
        return addOrDeleteStatus.toLong()
    }

    override suspend fun deleteLocation(savedLocation: SavedLocation): Int {
        savedLocations.remove(savedLocation)
        return addOrDeleteStatus
    }

    companion object {
        val savedLocations = mutableListOf(
            SavedLocation("Cairo", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("London", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Fayoum", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Paris", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Moscow", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Vienna", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Tokyo", 30.0, 60.0, 36, "google.com", "rainy"),
        )
    }
}