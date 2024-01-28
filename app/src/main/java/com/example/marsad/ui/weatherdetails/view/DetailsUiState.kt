package com.example.marsad.ui.weatherdetails.view

import com.example.marsad.domain.models.WeatherDetails

sealed class DetailsUiState {
    data class Success(val details: WeatherDetails) : DetailsUiState()
    data object Loading : DetailsUiState()
    data object Error : DetailsUiState()
}