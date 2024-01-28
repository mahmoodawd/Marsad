package com.example.marsad.data.repositories

import com.example.marsad.domain.models.FavoriteLocation
import com.example.marsad.domain.repositories.LocationRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeLocationRepository : LocationRepositoryInterface {

    private val savedLocationsFlow = MutableStateFlow(mutableListOf<FavoriteLocation>())


    override fun getSavedLocations(): Flow<List<FavoriteLocation>> =
        savedLocationsFlow

    fun sendFavoritesLocations(locations: List<FavoriteLocation>) {
        savedLocationsFlow.tryEmit(locations.toMutableList())
    }

    override suspend fun addLocation(savedLocation: FavoriteLocation): Long {
        savedLocationsFlow.update {
            (it + savedLocation).toMutableList()
        }
        return 1
    }

    override suspend fun deleteLocation(savedLocation: FavoriteLocation): Int {
        savedLocationsFlow.update {
            it.filterNot { location -> location == savedLocation }.toMutableList()
        }
        return 1
    }
}
