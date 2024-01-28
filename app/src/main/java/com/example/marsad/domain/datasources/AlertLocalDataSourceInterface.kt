package com.example.marsad.domain.datasources

import com.example.marsad.data.database.entities.AlertEntity
import kotlinx.coroutines.flow.Flow

interface AlertLocalDataSourceInterface {
    fun getItemById(itemId: Int): Flow<AlertEntity>
    fun getAllItems(): Flow<List<AlertEntity>>
    suspend fun addNewItem(item: AlertEntity): Long
    suspend fun deleteItem(item: AlertEntity): Int
}