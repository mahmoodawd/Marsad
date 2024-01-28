package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.AlertsDao
import com.example.marsad.data.database.entities.AlertEntity
import com.example.marsad.domain.datasources.AlertLocalDataSourceInterface
import kotlinx.coroutines.flow.Flow

class AlertLocalDataSource(val context: Context) : AlertLocalDataSourceInterface {

    private val alertsDao: AlertsDao by lazy {
        AppDatabase.getInstance(context).getAlertsDao()
    }

    override fun getItemById(itemId: Int): Flow<AlertEntity> = alertsDao.getAlertById(itemId)
    override fun getAllItems(): Flow<List<AlertEntity>> =
        alertsDao.getAllAlerts()

    override suspend fun addNewItem(item: AlertEntity) = alertsDao.insert(item)

    override suspend fun deleteItem(item: AlertEntity) = alertsDao.delete(item)


}