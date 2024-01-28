package com.example.marsad.data.repositories

import com.example.marsad.core.utils.FAHRENHEIT
import com.example.marsad.core.utils.KELVIN
import com.example.marsad.core.utils.MILES_HOUR
import com.example.marsad.domain.models.MeasureUnit
import com.example.marsad.domain.models.UserPrefs
import com.example.marsad.domain.repositories.UserPrefsRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakePrefsRepository : UserPrefsRepositoryInterface {
    override fun updatePrefs() {
        TODO("Not yet implemented")
    }

    private val prefsStateFlow = MutableStateFlow(UserPrefs())
    override val prefs: MutableStateFlow<UserPrefs>
        get() = prefsStateFlow

    override fun updateLatLon(lat: Double, lon: Double) {
        prefsStateFlow.update { it.copy(lat = lat, lon = lon) }
    }

    override fun updatePreferredLocationMethod(method: String) {
        prefsStateFlow.update { it.copy(preferredLocationMethod = method) }
    }

    override fun updateTempUnit(unit: String) {
        prefs.update {
            it.copy(
                tempUnit = when (unit) {
                    FAHRENHEIT -> MeasureUnit.Fahrenheit
                    KELVIN -> MeasureUnit.Kelvin
                    else -> MeasureUnit.Celsius
                }
            )
        }
    }

    override fun updateSpeedUnit(unit: String) {

        prefs.update {
            it.copy(
                speedUnit = when (unit) {
                    MILES_HOUR -> MeasureUnit.MilesPerHour
                    else -> MeasureUnit.MeterPerSecond
                }
            )
        }
    }

    override fun resetFirstUse() =
        prefs.update { it.copy(isFirstUse = true) }

    override fun updateLanguage(langTag: String) {
        prefs.update { it.copy(language = langTag) }
    }

}
