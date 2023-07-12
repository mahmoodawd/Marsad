package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.LocalSource
import com.example.marsad.data.network.RemoteSource
import com.example.marsad.data.network.WeatherDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherDetailsRepository private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource<WeatherDetailsResponse>
) : WeatherDetailsRepositoryInterface {

    companion object {
        private var instance: WeatherDetailsRepository? = null
        fun getInstance(
            remoteSource: RemoteSource, localSource: LocalSource<WeatherDetailsResponse>
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
        forceUpdate: Boolean
    ): Flow<WeatherDetailsResponse> {

        if (forceUpdate) {
            updateCachedResponse(lat, lon)
        }
        return localSource.getAllItems().flatten().flowOn(Dispatchers.IO)
    }

    private suspend fun updateCachedResponse(lat: Double, lon: Double) {
        try {

            val weatherDetailsResponse = remoteSource.getWeatherStatus(lat, lon)

            if (weatherDetailsResponse.isSuccessful) {
                weatherDetailsResponse.body()?.let { localSource.addNewItem(it) }
                println("Cache updated")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<List<T>>.flatten(): Flow<T> {
    return this.flatMapConcat { list -> flow { emit(list.last()) } }
}