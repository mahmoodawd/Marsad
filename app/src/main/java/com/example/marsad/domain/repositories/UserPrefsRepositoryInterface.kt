package com.example.marsad.domain.repositories

import com.example.marsad.domain.models.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow

interface UserPrefsRepositoryInterface {

    val prefs: MutableStateFlow<UserPrefs>
    fun updateLatLon(lat: Double, lon: Double)
    fun updatePreferredLocationMethod(method: String)
    fun updatePrefs()
    fun updateTempUnit(unit: String)
    fun updateSpeedUnit(unit: String)
    fun resetFirstUse()
    fun updateLanguage(langTag: String)
}

