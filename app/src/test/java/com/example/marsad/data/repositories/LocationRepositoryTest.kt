package com.example.marsad.data.repositories

import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.data.database.localdatasources.FakeLocationLocalDataSource
import com.example.marsad.data.mappers.toFavoriteLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var subject: LocationRepository
    private lateinit var locationLocalDataSource: FakeLocationLocalDataSource


    @Before
    fun setUp() {
        locationLocalDataSource = FakeLocationLocalDataSource()
        subject =
            LocationRepository.getInstance(locationLocalDataSource)
    }

    @Test
    fun locationRepository_saved_locations_stream_is_backed_by_location_local_data_source() {

        runTest {
            assertEquals(
                locationLocalDataSource.getAllItems().first()
                    .map(LocationEntity::toFavoriteLocation),
                subject.getSavedLocations().first()
            )
        }

    }

    @Test
    fun locationRepository_add_new_location_location_list_updated() {
        //Given savedLocationItem and old savedLocationList size
        val savedLocation =
            LocationEntity(-33.8688, 151.2093, "Sydney", "Australia", 78, "rain", "Rainy")
        runTest {
            val oldSize = locationLocalDataSource.getAllItems().first().size
            //When adding to local list
            subject.addLocation(savedLocation.toFavoriteLocation())
            assertEquals(
                oldSize.inc(),
                locationLocalDataSource.getAllItems().first().size
            )
            assertTrue(
                subject.getSavedLocations().first().contains(savedLocation.toFavoriteLocation())
            )
        }
    }


    @Test
    fun locationRepository_remove_existing_location_location_list_updated() {

        testScope.runTest {

            val oldSize = locationLocalDataSource.getAllItems().first().size
            val lastLocation =
                locationLocalDataSource.getAllItems().first().last().toFavoriteLocation()
            subject.deleteLocation(
                lastLocation
            )
            assertEquals(oldSize.dec(), locationLocalDataSource.getAllItems().first().size)
            assertFalse(subject.getSavedLocations().first().contains(lastLocation))
        }
    }

    @Test
    fun locationRepository_remove_existing_location_location_is_null() {

        testScope.runTest {
            val lastLocation = locationLocalDataSource.getAllItems().first().last()
            subject.deleteLocation(lastLocation.toFavoriteLocation())

            assertNull(locationLocalDataSource.getAllItems().first().find { it == lastLocation })
        }
    }


}