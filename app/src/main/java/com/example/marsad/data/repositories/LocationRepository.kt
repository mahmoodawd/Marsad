package com.example.marsad.data.repositories

import com.example.marsad.data.database.LocalSource
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.network.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationRepository private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : LocationRepositoryInterface {

    companion object {
        private var instance: LocationRepository? = null
        fun getInstance(
            remoteSource: RemoteSource, localSource: LocalSource
        ): LocationRepository {
            if (instance == null) {
                instance = LocationRepository(remoteSource, localSource)
            }
            return instance as LocationRepository
        }
    }

    override suspend fun getWeatherStatus(lat: Double, lon: Double): Flow<OneCallResponse> {

        return flow {
            emit(
                remoteSource.getWeatherStatus(lat, lon).body()
            )
        }.flowOn(Dispatchers.IO) as Flow<OneCallResponse>
    }

    override suspend fun getLocationWeather(lat: Double, lon: Double) = flow {
        emit(
            remoteSource.getLocationWeather(lat, lon).body()
        )
    }.flowOn(Dispatchers.IO)

    override fun getSavedLocations() = localSource.getAllLocations().flowOn(Dispatchers.IO)

    override suspend fun addLocation(savedLocation: SavedLocation) =
        localSource.addLocation(savedLocation)

    override suspend fun deleteLocation(savedLocation: SavedLocation) =
        localSource.deleteLocation(savedLocation)
}