package com.example.marsad.ui.weatherdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.core.utils.round
import com.example.marsad.data.network.Resource
import com.example.marsad.data.network.asResource
import com.example.marsad.domain.repositories.UserPrefsRepositoryInterface
import com.example.marsad.domain.repositories.WeatherDetailsRepositoryInterface
import com.example.marsad.ui.weatherdetails.view.DetailsUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    private val weatherDetailsRepository: WeatherDetailsRepositoryInterface,
    private val userPrefsRepository: UserPrefsRepositoryInterface,
) : ViewModel() {
    companion object {
        private const val TAG = "DetailsViewModel"
    }


    val userPrefs = userPrefsRepository.prefs.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherDetails: StateFlow<DetailsUiState> =
        userPrefs.flatMapLatest { prefs ->
            weatherDetailsRepository.getWeatherDetails(
                prefs.lat.round(4),
                prefs.lon.round(4),
                prefs.language
            ).asResource()
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> DetailsUiState.Success(resource.data!!)

                        is Resource.Failure -> DetailsUiState.Error

                        Resource.Loading -> DetailsUiState.Loading
                    }

                }
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = DetailsUiState.Loading,
                started = SharingStarted.WhileSubscribed(5000)
            )

    fun onLatLonChanged(lat: Double, lon: Double) {
        userPrefsRepository.updateLatLon(lat, lon)
    }
}


