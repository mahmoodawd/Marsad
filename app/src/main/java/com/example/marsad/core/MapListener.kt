package com.example.marsad.core

import android.content.Context
import androidx.appcompat.widget.SearchView
import com.example.marsad.core.utils.getLatLngFromLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapListener(private val context: Context, private val onMapClicked: (LatLng) -> Unit) :
    OnMapReadyCallback,
    SearchView.OnQueryTextListener {

    private lateinit var marsadGoogleMap: GoogleMap
    override fun onMapReady(googleMap: GoogleMap) {
        marsadGoogleMap = googleMap
        marsadGoogleMap.setOnMapClickListener { latLng ->
            MarkerOptions().apply {
                position(latLng)
                marsadGoogleMap.clear()
                val marker = marsadGoogleMap.addMarker(this)
                marker?.title = title
            }
            marsadGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0F))
            onMapClicked(latLng)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val location = query.toString()

        val latLon = getLatLngFromLocation(context, location)
        marsadGoogleMap.addMarker(MarkerOptions().position(latLon).title(location))
        marsadGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 10.0f))
        onMapClicked(latLon)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}