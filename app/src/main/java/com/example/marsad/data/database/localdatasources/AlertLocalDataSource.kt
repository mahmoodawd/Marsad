package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.AlertsDao
import com.example.marsad.data.model.AlertItem
import kotlinx.coroutines.flow.Flow

class AlertLocalDataSource(val context: Context) : LocalSource<AlertItem> {
    private val alertsDao: AlertsDao by lazy {
        AppDatabase.getInstance(context).getAlertsDao()
    }

    override fun getAllItems(): Flow<List<AlertItem>> = alertsDao.getAllAlerts()

    override fun getItemById(itemId: Long): Flow<AlertItem> = alertsDao.getAlertById(itemId)

    override suspend fun addNewItem(item: AlertItem) = alertsDao.insert(item)

    override suspend fun deleteItem(item: AlertItem) = alertsDao.delete(item)
}