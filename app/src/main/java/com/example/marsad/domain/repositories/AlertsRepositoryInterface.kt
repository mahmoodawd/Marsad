package com.example.marsad.domain.repositories


import com.example.marsad.domain.models.Alert
import com.example.marsad.domain.models.NotificationAlert
import kotlinx.coroutines.flow.Flow

interface AlertsRepositoryInterface {
    fun getActiveAlerts(): Flow<List<Alert>>
    fun getAlertById(id: Long): Flow<Alert>
    suspend fun addNewAlert(alertItem: Alert): Long
    suspend fun deleteAlert(alertItem: Alert): Int
    suspend fun checkForAlerts(lat: Double, lon: Double): Flow<NotificationAlert>

}