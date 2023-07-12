package com.example.marsad.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.data.repositories.AlertsRepositoryInterface
import com.example.marsad.data.repositories.LocationRepositoryInterface
import com.example.marsad.data.repositories.WeatherDetailsRepositoryInterface
import com.example.marsad.ui.favorites.viewmodel.FavoritesViewModel
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel

class MyViewModelFactory<T>(private val _repo: T) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(_repo as WeatherDetailsRepositoryInterface) as T
        } else if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            FavoritesViewModel(_repo as LocationRepositoryInterface) as T
        } else if (modelClass.isAssignableFrom(WeatherAlertsViewModel::class.java)) {
            WeatherAlertsViewModel(_repo as AlertsRepositoryInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("No Such ViewModel")
        }
    }
}