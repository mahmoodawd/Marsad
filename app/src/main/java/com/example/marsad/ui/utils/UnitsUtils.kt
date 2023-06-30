package com.example.marsad.ui.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager
import com.example.marsad.R
import java.text.NumberFormat
import java.util.Locale

object UnitsUtils {

    fun getTempRepresentation(context: Context, temp: Double, withSuffix: Boolean = true): String {
        val convertedTemp: Int
        var suffix = ""
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        when (prefs.getString(context.getString(R.string.temperature), "celsius")) {
            "kelvin" -> {
                convertedTemp = convertToKelvin(temp)
                if (withSuffix) suffix = context.getString(R.string.kelvin_suffix)
            }
            "fahrenheit" -> {
                convertedTemp = convertToFahrenheit(temp)
                if (withSuffix) suffix = context.getString(R.string.fahrenheit_suffix)
            }
            else -> {
                convertedTemp = temp.toInt()
                if (withSuffix) suffix = context.getString(R.string.celsius_suffix)
            }
        }
        return StringBuilder().append(localizeNumber(convertedTemp), suffix).toString()
    }

    private fun convertToFahrenheit(temp: Double) = (temp * 1.8 + 32).toInt()

    private fun convertToKelvin(temp: Double) = (temp * 273.15).toInt()

    fun getSpeedRepresentation(context: Context, speed: Double): String {
        val convertedSpeed: Double
        val suffix: String
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        when (prefs.getString(context.getString(R.string.wind_speed), "celsius")) {
            "miles_hour" -> {
                convertedSpeed = convertToMPH(speed)
                suffix = context.getString(R.string.miles_hour)
            }
            else -> {
                convertedSpeed = speed
                suffix = context.getString(R.string.meter_sec)
            }
        }

        return StringBuilder().append(
            localizeNumber(convertedSpeed),
            suffix
        ).toString()
    }

    fun localizeNumber(number: Number): String {
        val numberFormat =
            NumberFormat.getNumberInstance(UnitsUtils.getCurrentLocale()!!)
        return numberFormat.format(number)
    }

    private fun roundToTwoDecimalPlaces(convertedSpeed: Double) =
        "%.2f".format(convertedSpeed, Locale.US)
            .toDouble()

    private fun convertToMPH(speed: Double) = speed * 2.23694

    fun getCurrentLocale(): Locale =
        AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()
}



