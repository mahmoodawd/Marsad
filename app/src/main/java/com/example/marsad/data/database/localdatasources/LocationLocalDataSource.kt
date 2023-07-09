package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.model.SavedLocation
import kotlinx.coroutines.flow.Flow

class LocationLocalDataSource(val context: Context) : LocalSource<SavedLocation> {
    private val locationDoa: LocationDoa by lazy {
        AppDatabase.getInstance(context).getLocationDao()
    }

    override fun getAllItems() = locationDoa.getAll()


    override suspend fun addNewItem(item: SavedLocation) =
        locationDoa.insert(item)

    override suspend fun deleteItem(item: SavedLocation) =
        locationDoa.delete(item)

    override fun getItemById(itemId: Long): Flow<SavedLocation> {
        TODO("Not yet implemented")
    }
}