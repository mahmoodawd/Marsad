package com.example.marsad.data.repositories

import com.example.marsad.data.database.LocalSource
import com.example.marsad.data.network.RemoteSource
import com.example.marsad.data.network.WeatherResponse
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

    override suspend fun getWeatherStatus(lat: String, lon: String): Flow<WeatherResponse> {

        return flow { emit(remoteSource.getWeatherStatus(lat, lon).body()) }.flowOn(Dispatchers.IO) as Flow<WeatherResponse>
    }
}