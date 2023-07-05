package com.example.marsad.data.repositories


import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.network.OpenWeatherMapResponse
import kotlinx.coroutines.flow.Flow

interface AlertRepositoryInterface {
    fun getActiveAlerts(): Flow<List<AlertItem>>
    suspend fun addNewAlert(alertItem: AlertItem): Long
    suspend fun deleteAlert(alertItem: AlertItem): Int
}