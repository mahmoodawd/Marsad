package com.example.marsad.data.database

import com.example.marsad.data.model.SavedLocation
import kotlinx.coroutines.flow.Flow


interface LocalSource {
    fun getAllLocations(): Flow<List<SavedLocation>>
    suspend fun addLocation(savedLocation: SavedLocation): Long
    suspend fun deleteLocation(savedLocation: SavedLocation): Int

}