package com.example.marsad.data.repositories

import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.data.mappers.toFavoriteLocation
import com.example.marsad.data.mappers.toLocationEntity
import com.example.marsad.domain.datasources.LocationLocalDataSourceInterface
import com.example.marsad.domain.models.FavoriteLocation
import com.example.marsad.domain.repositories.LocationRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LocationRepository private constructor(
    private val localSource: LocationLocalDataSourceInterface,
) : LocationRepositoryInterface {

    companion object {
        private var instance: LocationRepository? = null
        fun getInstance(
            localSource: LocationLocalDataSourceInterface,
        ): LocationRepository {
            if (instance == null) {
                instance = LocationRepository(localSource)
            }
            return instance as LocationRepository
        }
    }

    override fun getSavedLocations(): Flow<List<FavoriteLocation>> =
        localSource.getAllItems().map { locationEntities ->
            locationEntities.map(LocationEntity::toFavoriteLocation)
        }.flowOn(Dispatchers.IO)


    override suspend fun addLocation(savedLocation: FavoriteLocation) =
        localSource.addNewItem(savedLocation.toLocationEntity())

    override suspend fun deleteLocation(savedLocation: FavoriteLocation) =
        localSource.deleteItem(savedLocation.toLocationEntity())

}