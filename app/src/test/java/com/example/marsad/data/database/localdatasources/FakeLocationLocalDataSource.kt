package com.example.marsad.data.database.localdatasources

import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.domain.datasources.LocationLocalDataSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeLocationLocalDataSource : LocationLocalDataSourceInterface {

    private var locationEntitiesStateFlow = MutableStateFlow(
        listOf(
            LocationEntity(51.5074, -0.1278, "London", "United Kingdom", 55, "cloudy", "Cloudy"),
            LocationEntity(35.6895, 139.6917, "Tokyo", "Japan", 60, "snow", "Snowy"),
            LocationEntity(-22.9068, -43.1729, "Rio de Janeiro", "Brazil", 82, "fog", "Foggy"),
            LocationEntity(55.7558, 37.6176, "Moscow", "Russia", 25, "wind", "Windy"),
            LocationEntity(40.4168, -3.7038, "Madrid", "Spain", 75, "hail", "Hailstorm"),
        )
    )


    override fun getAllItems(): Flow<List<LocationEntity>> =
        locationEntitiesStateFlow

    override suspend fun addNewItem(item: LocationEntity): Long {
        locationEntitiesStateFlow.update { oldValue ->
            oldValue + item

        }
        return 1
    }

    override suspend fun deleteItem(item: LocationEntity): Int {
        locationEntitiesStateFlow.update { oldValue ->
            oldValue.dropLast(1)
        }
        return 1
    }


}
