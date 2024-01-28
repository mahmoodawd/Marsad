package com.example.marsad.domain.models

import androidx.annotation.StringRes
import com.example.marsad.R
import com.example.marsad.core.utils.CELSIUS
import com.example.marsad.core.utils.ENGLISH
import com.example.marsad.core.utils.FAHRENHEIT
import com.example.marsad.core.utils.KELVIN
import com.example.marsad.core.utils.METER_SEC
import com.example.marsad.core.utils.MILES_HOUR
import com.example.marsad.core.utils.UNDEFINED


data class UserPrefs(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    val language: String = ENGLISH,
    var isFirstUse: Boolean = true,
    var isHavingLocation: Boolean = false,
    var preferredLocationMethod: String = UNDEFINED,
    var tempUnit: MeasureUnit = MeasureUnit.Celsius,
    var speedUnit: MeasureUnit = MeasureUnit.MeterPerSecond,
)

private const val DEFAULT_FACTOR = 1.0
private const val KELVIN_FACTOR = 273.15
private const val FAHRENHEIT_FACTOR = 1.8 + 32
private const val MILES_HOUR_FACTOR = 2.23694


enum class MeasureUnit(val title: String, @StringRes val symbol: Int, val factor: Double) {
    Celsius(CELSIUS, R.string.celsius_suffix, DEFAULT_FACTOR),
    Fahrenheit(FAHRENHEIT, R.string.fahrenheit_suffix, FAHRENHEIT_FACTOR),
    Kelvin(KELVIN, R.string.kelvin_suffix, KELVIN_FACTOR),
    MeterPerSecond(METER_SEC, R.string.meter_sec, DEFAULT_FACTOR),
    MilesPerHour(MILES_HOUR, R.string.miles_hour, MILES_HOUR_FACTOR)
}