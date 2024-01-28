package com.example.marsad.domain.repositories


import com.example.marsad.domain.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryInterface {

    fun getSavedLocations(): Flow<List<FavoriteLocation>>
    suspend fun addLocation(savedLocation: FavoriteLocation): Long
    suspend fun deleteLocation(savedLocation: FavoriteLocation): Int

}