package com.example.marsad.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

private const val My_LOCATION_PERMISSION_ID = 5005

class LocationManager(
    private val context: Context,
    private var client: FusedLocationProviderClient,
    val onLocationResult: (lat: Double, lon: Double) -> Unit,
) {
    fun getLastLocation() {
        if (!isLocationPermissionsGranted()) throw LocationException.LocationPermissionException()

        if (!isLocationEnabled()) throw LocationException.LocationDisabledException()

        requestNewLocationData()
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun askUserToEnableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener {
            onLocationResult(it.latitude, it.longitude)
        }.addOnFailureListener {
            it.printStackTrace()
        }

    }


    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), My_LOCATION_PERMISSION_ID
        )
    }

    fun stopLocationUpdates() {

        client.removeLocationUpdates { }.addOnSuccessListener {
            Log.d("LOCATION_TAG", "Location updates removed.")
        }.addOnFailureListener {
            Log.e("LOCATION_TAG", "Failed to remove Location updates")
            it.printStackTrace()
        }
    }
}

sealed class LocationException : Exception() {
    class LocationPermissionException : LocationException()
    class LocationDisabledException : LocationException()
}

