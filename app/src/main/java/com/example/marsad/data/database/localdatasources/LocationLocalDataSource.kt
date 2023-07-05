package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.model.SavedLocation

class LocationLocalDataSource(val context: Context) : LocalSource<SavedLocation> {
    private val locationDoa: LocationDoa by lazy {
        AppDatabase.getInstance(context).getLocationDao()
    }

    override fun getAllItems() = locationDoa.getAll()


    override suspend fun addNewItem(savedLocation: SavedLocation) =
        locationDoa.insert(savedLocation)

    override suspend fun deleteItem(savedLocation: SavedLocation) =
        locationDoa.delete(savedLocation)
}