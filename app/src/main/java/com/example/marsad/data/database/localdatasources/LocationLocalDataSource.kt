package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.domain.datasources.LocationLocalDataSourceInterface
import kotlinx.coroutines.flow.Flow

class LocationLocalDataSource(val context: Context) :
    LocationLocalDataSourceInterface {

    private val locationDoa: LocationDoa by lazy {
        AppDatabase.getInstance(context).getLocationDao()
    }

    override fun getAllItems(): Flow<List<LocationEntity>> =
        locationDoa.getFavoritesLocations()


    override suspend fun addNewItem(item: LocationEntity) =
        locationDoa.insert(item)

    override suspend fun deleteItem(item: LocationEntity) =
        locationDoa.delete(item)
}