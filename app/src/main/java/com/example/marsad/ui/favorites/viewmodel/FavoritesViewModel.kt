package com.example.marsad.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.network.Resource
import com.example.marsad.data.network.asResource
import com.example.marsad.domain.models.FavoriteLocation
import com.example.marsad.domain.repositories.LocationRepositoryInterface
import com.example.marsad.ui.favorites.view.FavUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: LocationRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val favLocationsUiState: StateFlow<FavUiState> = repository.getSavedLocations()
        .asResource()
        .map { state ->
            when (state) {
                is Resource.Failure -> FavUiState.Error

                Resource.Loading -> FavUiState.Loading

                is Resource.Success -> {
                    val locations = (state.data)
                    if (locations.isEmpty()) FavUiState.Empty
                    else FavUiState.Success(locations.reversed())
                }
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = FavUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )


    fun addLocation(savedLocation: FavoriteLocation) =
        viewModelScope.launch(dispatcher) {
            repository.addLocation(savedLocation)
        }


    fun removeLocation(savedLocation: FavoriteLocation) =
        viewModelScope.launch(dispatcher) {
            repository.deleteLocation(savedLocation)
        }


}