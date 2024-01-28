package com.example.marsad.core.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


/**
 * Helpers methods converts between Addresses, LatLon, and location instances
 */
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
    val geocoder = Geocoder(context, Locale.getDefault())
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
