package com.example.marsad.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.marsad.R
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.net.URL
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
            localizeNumber(convertedSpeed), " ",
            suffix
        ).toString()
    }

    fun localizeNumber(number: Number): String {
        val numberFormat =
            NumberFormat.getNumberInstance(getCurrentLocale())
        return numberFormat.format(number)
    }

    private fun roundToTwoDecimalPlaces(convertedSpeed: Double) =
        "%.2f".format(convertedSpeed, Locale.US)
            .toDouble()

    private fun convertToMPH(speed: Double) = speed * 2.23694

    fun getCurrentLocale(): Locale =
        AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()

    fun getCity(context: Context, lat: Double, lon: Double): String {
        val address = getAddressFromLatAndLon(context, lat, lon)

        return StringBuilder().append(
            address?.countryName ?: "", ", ", address?.locality ?: ""
        ).toString()
    }

    fun getLatLngFromLocation(context: Context, location: String): LatLng {
        val address = getAddressFromLocation(context, location)

        return LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0)

    }

    private fun getAddressFromLocation(context: Context, location: String): Address? {

        var address: Address? = null
        val geocoder = Geocoder(context, getCurrentLocale())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(location, 1) { addresses ->
                address = addresses[0]
            }
        } else {
            try {
                geocoder.getFromLocationName(location, 1)?.get(0)?.let { address = it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return address
    }

    private fun getAddressFromLatAndLon(context: Context, lat: Double, lon: Double): Address? {
        var address: Address? = null
        val geocoder = Geocoder(context, getCurrentLocale())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lon, 1) { addresses ->
                address = addresses[0]
            }
        } else {
            try {

                geocoder.getFromLocation(lat, lon, 1)?.get(0)?.let { address = it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return address
    }

    private fun getTimeZoneId(lat: Double, lon: Double): String {
        val timestamp = System.currentTimeMillis() / 1000 // Convert to seconds
        val url =
            "https://maps.googleapis.com/maps/api/timezone/json?location=$lat,$lon&timestamp=$timestamp&key={API key}"
        val response = URL(url).readText()
        val jsonObject = JSONObject(response)
        return jsonObject.getString("timeZoneId")
    }
}



