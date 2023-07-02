package com.example.marsad.data.database

import android.content.Context
import com.example.marsad.data.model.SavedLocation
import kotlinx.coroutines.flow.Flow

class LocationLocalDataSource(val context: Context) : LocalSource {
    private val locationDoa: LocationDoa by lazy {
        AppDatabase.getInstance(context).getLocationDao()
    }

    override fun getAllLocations() = locationDoa.getAll()


    override suspend fun addLocation(savedLocation: SavedLocation) =
        locationDoa.insert(savedLocation)

    override suspend fun deleteLocation(savedLocation: SavedLocation) =
        locationDoa.delete(savedLocation)
}