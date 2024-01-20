package com.example.marsad.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.example.marsad.core.utils.GPS
import com.example.marsad.core.utils.MAP
import com.example.marsad.domain.repositories.UserPrefsRepositoryInterface
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val userPrefsRepository: UserPrefsRepositoryInterface,
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    val userPrefsState = userPrefsRepository.prefs.asStateFlow()


    fun preferGPSMethod() {
        userPrefsRepository.updatePreferredLocationMethod(GPS)
    }

    fun preferMapMethod() {
        userPrefsRepository.updatePreferredLocationMethod(MAP)
    }

    fun onLocationChanged(latitude: Double, longitude: Double) {
        userPrefsRepository.updateLatLon(latitude, longitude)
    }

    fun updatePrefs() {
        userPrefsRepository.updatePrefs()
    }

}


