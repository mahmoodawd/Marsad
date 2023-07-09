package com.example.marsad.data.repositories


import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.network.AlertResponse
import kotlinx.coroutines.flow.Flow

interface AlertsRepositoryInterface {
    fun getActiveAlerts(): Flow<List<AlertItem>>
    fun getAlertById(id: Long): Flow<AlertItem>
    suspend fun addNewAlert(alertItem: AlertItem): Long
    suspend fun deleteAlert(alertItem: AlertItem): Int
    suspend fun getWeatherAlerts(lat: Double, lon: Double): Flow<AlertResponse>

}