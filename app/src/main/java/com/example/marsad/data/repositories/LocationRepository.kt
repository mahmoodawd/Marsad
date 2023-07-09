package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.LocalSource
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.network.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationRepository private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource<SavedLocation>
) : LocationRepositoryInterface {

    companion object {
        private var instance: LocationRepository? = null
        fun getInstance(
            remoteSource: RemoteSource, localSource: LocalSource<SavedLocation>
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

    override fun getSavedLocations() = localSource.getAllItems().flowOn(Dispatchers.IO)

    override suspend fun addLocation(savedLocation: SavedLocation) =
        localSource.addNewItem(savedLocation)

    override suspend fun deleteLocation(savedLocation: SavedLocation) =
        localSource.deleteItem(savedLocation)
}