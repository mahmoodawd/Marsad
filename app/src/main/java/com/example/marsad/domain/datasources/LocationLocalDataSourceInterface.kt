package com.example.marsad.domain.datasources

import com.example.marsad.data.database.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationLocalDataSourceInterface {
    fun getAllItems(): Flow<List<LocationEntity>>

    suspend fun addNewItem(item: LocationEntity): Long

    suspend fun deleteItem(item: LocationEntity): Int
}