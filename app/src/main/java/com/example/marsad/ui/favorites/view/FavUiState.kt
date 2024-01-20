package com.example.marsad.ui.favorites.view

import com.example.marsad.domain.models.FavoriteLocation

sealed class FavUiState {
    data class Success(val favoriteLocations: List<FavoriteLocation>) : FavUiState()
    data object Loading : FavUiState()
    data object Empty : FavUiState()
    data object Error : FavUiState()
}