package com.example.marsad.ui.favorites.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marsad.data.repositories.FakeLocationRepository
import com.example.marsad.domain.models.FavoriteLocation
import com.example.marsad.ui.favorites.view.FavUiState
import com.example.marsad.util.MainDispatcherRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private val locationsRepo = FakeLocationRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = UnconfinedTestDispatcher()


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        favoritesViewModel = FavoritesViewModel(repository = locationsRepo, dispatcher = testScope)

    }

    @Test
    fun favoriteViewModel_getInitialState_stateIsInitiallyLoading() = runTest {
        assertEquals(FavUiState.Loading, favoritesViewModel.favLocationsUiState.value)
    }

    @Test
    fun favoriteViewModel_loadingLocations_stateIsLoading() = runTest {

        locationsRepo.sendFavoritesLocations(sampleLocations)

        assertEquals(FavUiState.Loading, favoritesViewModel.favLocationsUiState.value)

    }

    @Test
    fun favoriteViewModel_afterLoadingLocations_locationsAreShownReversed() = runTest {
        // Create empty collector to make the locations stateflow start emitting because of the stateIn operator
        val collectJob = launch(testScope) { favoritesViewModel.favLocationsUiState.collect() }

        locationsRepo.sendFavoritesLocations(sampleLocations)

        assertEquals(
            FavUiState.Success(sampleLocations.reversed()),
            favoritesViewModel.favLocationsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun favoriteViewModel_loadingEmptyLocations_stateIsEmpty() = runTest {
        // Create empty collector to make the locations stateflow start emitting because of the stateIn operator
        val collectJob = launch(testScope) { favoritesViewModel.favLocationsUiState.collect() }

        locationsRepo.sendFavoritesLocations(emptyList())

        assertEquals(
            FavUiState.Empty,
            favoritesViewModel.favLocationsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun favoriteViewModel_addingNewLocation_locationsAreUpdated() = runTest {
        val collectJob = launch(testScope) { favoritesViewModel.favLocationsUiState.collect() }

        locationsRepo.sendFavoritesLocations(sampleLocations)
        favoritesViewModel.addLocation(
            FavoriteLocation(51.5099, -0.1180, "London", "United Kingdom"),
        )
        assertEquals(
            FavUiState.Success(
                (sampleLocations +
                        FavoriteLocation(
                            51.5099,
                            -0.1180,
                            "London",
                            "United Kingdom"
                        )).reversed()

            ),
            favoritesViewModel.favLocationsUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun favoriteViewModel_removingLocation_locationsAreUpdated() = runTest {
        val collectJob = launch(testScope) { favoritesViewModel.favLocationsUiState.collect() }

        locationsRepo.sendFavoritesLocations(sampleLocations)
        favoritesViewModel.removeLocation(
            FavoriteLocation(51.5099, -0.1180, "London", "United Kingdom"),
        )
        assertEquals(
            FavUiState.Success(
                (sampleLocations.filterNot {
                    it == FavoriteLocation(
                        51.5099,
                        -0.1180,
                        "London",
                        "United Kingdom"
                    )
                }).reversed()

            ),
            favoritesViewModel.favLocationsUiState.value
        )

        collectJob.cancel()
    }
}


private val sampleLocations = listOf(
    FavoriteLocation(37.7749, -122.4194, "San Francisco", "United States"),
    FavoriteLocation(40.7128, -74.0060, "New York City", "United States"),
    FavoriteLocation(51.5099, -0.1180, "London", "United Kingdom"),
    FavoriteLocation(48.8566, 2.3522, "Paris", "France"),
    FavoriteLocation(35.6895, 139.6917, "Tokyo", "Japan"),
    FavoriteLocation(-33.8688, 151.2093, "Sydney", "Australia"),
    FavoriteLocation(-23.5505, -46.6333, "São Paulo", "Brazil"),
    FavoriteLocation(55.7558, 37.6176, "Moscow", "Russia"),
    FavoriteLocation(40.4168, -3.7038, "Madrid", "Spain"),
    FavoriteLocation(-34.6037, -58.3816, "Buenos Aires", "Argentina")
)