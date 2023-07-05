package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.LocalSource
import com.example.marsad.data.model.AlertItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AlertRepository(val localSource: LocalSource<AlertItem>) : AlertRepositoryInterface {

    companion object {
        private var instance: AlertRepository? = null
        fun getInstance(localSource: LocalSource<AlertItem>): AlertRepository {
            if (instance == null) {
                instance = AlertRepository(localSource)
            }
            return instance as AlertRepository
        }
    }

    override fun getActiveAlerts(): Flow<List<AlertItem>> =
        localSource.getAllItems().flowOn(Dispatchers.IO)

    override suspend fun addNewAlert(alertItem: AlertItem) = localSource.addNewItem(alertItem)


    override suspend fun deleteAlert(alertItem: AlertItem) = localSource.deleteItem(alertItem)
}