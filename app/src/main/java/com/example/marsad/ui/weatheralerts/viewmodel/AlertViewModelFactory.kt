package com.example.marsad.ui.weatheralerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.data.repositories.AlertsRepositoryInterface

class AlertViewModelFactory(private val _repo: AlertsRepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherAlertsViewModel::class.java)) {
            WeatherAlertsViewModel(_repo) as T
        } else {
            throw java.lang.IllegalArgumentException("No Such ViewModel")
        }
    }
}