package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.FakeLocationLocalDataSource
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.FakeWeatherRemoteDataSource
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.network.OpenWeatherMapResponse
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

class LocationRepositoryTest {
    private lateinit var repository: LocationRepository
    private lateinit var fakeWeatherRemoteDataSource: FakeWeatherRemoteDataSource
    private lateinit var fakeLocationLocalDataSource: FakeLocationLocalDataSource
    private var locationsList = FakeLocationLocalDataSource.savedLocations

    @Before
    fun setUp() {
        fakeWeatherRemoteDataSource = FakeWeatherRemoteDataSource()
        fakeLocationLocalDataSource = FakeLocationLocalDataSource()
        repository =
            LocationRepository.getInstance(fakeWeatherRemoteDataSource, fakeLocationLocalDataSource)
    }

    @Test
    fun getSavedLocations_savedLocations() {
        //Given empty locationList
        var savedLocations = listOf<SavedLocation>()
        //When request saved Locations
        runTest {
            repository.getSavedLocations().collect {
                savedLocations = it
            }
            //Then get local source stored locations
            assertEquals(locationsList, savedLocations)
        }

    }

    @Test
    fun addNewLocation_newLocation_localListIncrementedByOne() {
        //Given savedLocationItem old savedLocationList size
        val savedLocation =
            SavedLocation("Washington", 88.0, 33.0, 8, "google.com", "rainy")
        val oldSize = locationsList.size
        //When adding to local list
        runTest {
            val newSize = repository.addLocation(savedLocation).toInt()
            assertEquals(oldSize + 1, newSize)
        }
    }

    @Test
    fun deleteLocation_existingLocation_localListDecrementedByOne() {
        //Given savedLocationItem old savedLocationList size
        val savedLocation =
            SavedLocation("Cairo", 30.0, 60.0, 36, "google.com", "rainy")
        val oldSize = locationsList.size
        //When deleting from  local list
        runTest {
            val newSize = repository.deleteLocation(savedLocation)
            assertEquals(oldSize - 1, newSize)
        }
    }

    @Test
    fun getWeatherDetails_locationLatAndLon_OneCallResponse() {
        var response: OneCallResponse? = null
        runTest {
            repository.getWeatherDetails(30.0, 50.0).collect {
                response = it
            }
            assertEquals(fakeWeatherRemoteDataSource.fakeOneCallResponse, response)
        }
    }

    @Test
    fun getLocationWeather_locationLatAndLon_openWeatherMapResponseNotnull() {
        var response: OpenWeatherMapResponse? = null
        runTest {
            repository.getLocationWeather(30.0, 50.0).collect {
                response = it
            }
            assertThat(response, not(nullValue()))
        }

    }
}