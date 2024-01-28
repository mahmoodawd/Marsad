package com.example.marsad.ui.weatherdetails.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marsad.data.repositories.FakeDetailsRepository
import com.example.marsad.data.repositories.FakePrefsRepository
import com.example.marsad.domain.models.City
import com.example.marsad.domain.models.Main
import com.example.marsad.domain.models.TimeStamp
import com.example.marsad.domain.models.Weather
import com.example.marsad.domain.models.WeatherDetails
import com.example.marsad.ui.weatherdetails.view.DetailsUiState
import com.example.marsad.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(AndroidJUnit4::class)
class DetailsViewModelTest {

    private lateinit var detailsViewModel: DetailsViewModel
    private val detailsRepo = FakeDetailsRepository()
    private val prefsRepo = FakePrefsRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        detailsViewModel = DetailsViewModel(detailsRepo, prefsRepo)
    }

    @Test
    fun detailsViewModel_getInitialState_stateInitiallyLoading() = runTest {

        assertEquals(DetailsUiState.Loading, detailsViewModel.weatherDetails.value)
    }

    @Test
    fun detailsViewModel_loadingDetails_stateIsLoading() = runTest {
        detailsRepo.sendDetails(detailsSample)
        assertEquals(DetailsUiState.Loading, detailsViewModel.weatherDetails.value)
    }

    @Test
    fun detailsViewModel_afterLoadingDetails_detailsLoaded() = runTest {
        val collectJob = launch(testScope) { detailsViewModel.weatherDetails.collect() }
        detailsRepo.sendDetails(detailsSample)
        assertEquals(DetailsUiState.Success(detailsSample), detailsViewModel.weatherDetails.value)
        collectJob.cancel()
    }

    @Test
    fun detailsViewModel_onChangingLatAndLon_detailsAndLocationUpdated() = runTest {
        val collectJob = launch(testScope) { detailsViewModel.weatherDetails.collect() }
        detailsRepo.sendDetails(detailsSample)
        prefsRepo.updateLatLon(5.5, 6.6)
        val detailsState = detailsViewModel.weatherDetails.value
        assertTrue(detailsState is DetailsUiState.Success)
        assertEquals(5.5, detailsViewModel.userPrefs.value.lat, 1.0)
        assertEquals(6.6, detailsViewModel.userPrefs.value.lon, 1.0)
        collectJob.cancel()
    }

    @Test
    fun detailsViewModel_onChangingLanguage_detailsAndLanguageUpdated() = runTest {
        val collectJob = launch(testScope) { detailsViewModel.weatherDetails.collect() }
        detailsRepo.sendDetails(detailsSample)
        prefsRepo.updateLanguage("ar")
        val detailsState = detailsViewModel.weatherDetails.value
        assertTrue(detailsState is DetailsUiState.Success)
        assertEquals(detailsViewModel.userPrefs.value.language, "ar")

        collectJob.cancel()
    }
}

private val detailsSample = WeatherDetails(
    dateTime = "2024-01-28T12:00:00",
    city = City(
        name = "Dummy City",
        country = "Dummy Country",
        timezone = 0,
        sunrise = 0,
        sunset = 0
    ),
    current = TimeStamp(
        dateTime = "2024-01-28T12:00:00",
        main = Main(
            temperature = 25,
            feelsLike = 22,
            tempMin = 20,
            tempMax = 30,
            pressure = 1010,
            humidity = 60
        ),
        weather = Weather(
            description = "Partly Cloudy",
            icon = "01d"
        ),
        clouds = 20,
        windSpeed = 5.0
    ),
    daily = listOf(),
    hourly = listOf()

)



