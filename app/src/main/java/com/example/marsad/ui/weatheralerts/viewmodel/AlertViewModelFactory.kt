package com.example.marsad.ui.weatheralerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.data.repositories.AlertRepositoryInterface
import com.example.marsad.data.repositories.LocationRepositoryInterface
import com.example.marsad.ui.favorites.viewmodel.FavoritesViewModel
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel

class AlertViewModelFactory(private val _repo: AlertRepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherAlertsViewModel::class.java)) {
            WeatherAlertsViewModel(_repo) as T
        } else {
            throw java.lang.IllegalArgumentException("No Such ViewModel")
        }
    }
}