package com.example.marsad.ui.settings.view

import androidx.lifecycle.ViewModel
import com.example.marsad.domain.repositories.UserPrefsRepositoryInterface
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val userPrefsRepository: UserPrefsRepositoryInterface) :
    ViewModel() {
    val prefs = userPrefsRepository.prefs.asStateFlow()
    fun updatePreferredTempUnit(unit: String) =
        userPrefsRepository.updateTempUnit(unit)


    fun updatePreferredSpeedUnit(unit: String) =
        userPrefsRepository.updateSpeedUnit(unit)

    fun updatePreferredLocationMethod(method: String) =
        userPrefsRepository.updatePreferredLocationMethod(method)

    fun resetFirstUse() = userPrefsRepository.resetFirstUse()

    fun updateLanguage(langTag: String) = userPrefsRepository.updateLanguage(langTag)

}