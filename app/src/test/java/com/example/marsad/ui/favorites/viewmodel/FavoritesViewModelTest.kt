package com.example.marsad.ui.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.Coord
import com.example.marsad.data.network.Main
import com.example.marsad.data.network.OpenWeatherMapResponse
import com.example.marsad.data.repositories.FakeAlertsRepository
import com.example.marsad.data.repositories.FakeLocationRepository
import com.example.marsad.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var fakeRepo: FakeLocationRepository
    private lateinit var favoritesList: MutableList<SavedLocation>


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        fakeRepo = FakeLocationRepository()
        favoritesViewModel = FavoritesViewModel(fakeRepo)
        favoritesList = FakeLocationRepository.savedLocations
    }

    @Test
    fun getSavedLocations_savedLocationsNotNull() {
        //When retrieving location from local
        val value = favoritesViewModel.getSavedLocations().getOrAwaitValue()
        //Then location should not be null
        assertThat(value, not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addLocation_newLocationItem_locationItemsIncreasedByOne() = runTest {
        //Given location Item
        val oldSize = favoritesList.size
        val savedLocation =
            SavedLocation("Washington", 88.0, 33.0, 8, "google.com", "rainy")
        //When adding the location to local
        favoritesViewModel.addLocation(savedLocation)
        advanceUntilIdle()
        //Then location should be added
        val newSize = favoritesList.size
        assertEquals(oldSize + 1, newSize)
    }


    @Ignore("conflict With Add")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeLocation_existingLocationItem_locationRemoved() = runTest {
        val oldSize = favoritesList.size
        //When deleting the location from local
        favoritesViewModel.removeLocation(FakeLocationRepository.savedLocations[0])
        advanceUntilIdle()
        //Then location should be deleted
        val newSize = favoritesList.size
        assertEquals(oldSize - 1, newSize)
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
