package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.LocalSource
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AlertsRepository private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource<AlertItem>
) : AlertsRepositoryInterface {

    companion object {
        private var instance: AlertsRepository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            localSource: LocalSource<AlertItem>
        ): AlertsRepository {
            if (instance == null) {
                instance = AlertsRepository(remoteSource, localSource)
            }
            return instance as AlertsRepository
        }
    }

    override fun getActiveAlerts(): Flow<List<AlertItem>> =
        localSource.getAllItems().flowOn(Dispatchers.IO)

    override fun getAlertById(id: Long): Flow<AlertItem> =
        localSource.getItemById(id).flowOn(Dispatchers.IO)

    override suspend fun addNewAlert(alertItem: AlertItem) = localSource.addNewItem(alertItem)

    override suspend fun deleteAlert(alertItem: AlertItem) = localSource.deleteItem(alertItem)

    override suspend fun getWeatherAlerts(lat: Double, lon: Double): Flow<AlertResponse> {
        return flow {
            emit(remoteSource.getWeatherAlerts(lat, lon).body())
        }.flowOn(Dispatchers.IO) as Flow<AlertResponse>
    }
}