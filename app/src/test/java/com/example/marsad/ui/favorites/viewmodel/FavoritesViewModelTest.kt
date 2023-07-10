package com.example.marsad.ui.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.Coord
import com.example.marsad.data.network.Main
import com.example.marsad.data.network.OpenWeatherMapResponse
import com.example.marsad.data.repositories.FakeLocationRepository
import com.example.marsad.data.repositories.LocationRepository
import com.example.marsad.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {
    lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var fakeRepo: FakeLocationRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        fakeRepo = FakeLocationRepository()
        favoritesViewModel = FavoritesViewModel(fakeRepo)
    }

    @Test
    fun getSavedLocations_savedLocationsNotNull() {
        //When retrieving location from local
        val value = favoritesViewModel.getSavedLocations().getOrAwaitValue()
        //Then location should not be null
        assertThat(value, not(nullValue()))
    }

    @Test
    fun addLocation_insertionStatusGreaterThanZero_locationAdded() {

        //Given location Item
        val savedLocation =
            SavedLocation("Washington", 88.0, 33.0, 8, "google.com", "rainy")
        fakeRepo.addOrDeleteStatus = 1
        //When adding the location to local
        val value = favoritesViewModel.addLocation(savedLocation).getOrAwaitValue()
        //Then location should be added
        assertTrue(value)
    }

    @Test
    fun addLocation_insertionStatusEqualsZero_locationNotAdded() {

        //Given location Item
        val savedLocation =
            SavedLocation("Washington", 88.0, 33.0, 8, "google.com", "rainy")
        fakeRepo.addOrDeleteStatus = 0
        //When adding the location to local
        val value = favoritesViewModel.addLocation(savedLocation).getOrAwaitValue()
        //Then location should not be added
        assertFalse(value)
    }


    @Test
    fun removeLocation_existingLocationItem_locationRemoved() {
        //When deleting the location from local
        val value = favoritesViewModel.removeLocation(FakeLocationRepository.savedLocations[0])
            .getOrAwaitValue()
        //Then location should be deleted
        assertTrue(value)

    }

    @Test
    fun getLocationWeather_emitNothing_ApiStateLoading() {
        runTest {
            //When calling getLocationWeather
            favoritesViewModel.getLocationWeather(
                lat = 0.0,
                lon = 1.0
            )
            //Then should be the initial state: Loading
            assertEquals(
                ApiState.Loading,
                favoritesViewModel.locationWeatherStateFlow.value
            )


        }
    }

    @Test
    fun getLocationWeather_emittingSomeValue_ApiStateSuccess() = runTest {
        //When calling getLocationWeather and emitting some value
        favoritesViewModel.getLocationWeather(
            lat = 0.0,
            lon = 1.0
        )
        //Then should be the Success state
        val response = OpenWeatherMapResponse(Coord(20.0, 30.0), Main(25.0), listOf())
        fakeRepo.emit(response)

        favoritesViewModel.locationWeatherStateFlow.test {
            assertEquals(ApiState.Success(response), awaitItem())
        }
    }
}
