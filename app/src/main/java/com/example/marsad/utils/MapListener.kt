package com.example.marsad.utils

import android.content.Context
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapListener(private val context: Context, private val onMapClicked: (LatLng) -> Unit) :
    OnMapReadyCallback,
    SearchView.OnQueryTextListener {
    private lateinit var mMap: GoogleMap
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            MarkerOptions().apply {
                position(it)
                mMap.clear()
                mMap.addMarker(this)
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10.0F))
            onMapClicked(it)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val location = query.toString()

        val latLon = UnitsUtils.getLatLngFromLocation(context, location)
        mMap.addMarker(MarkerOptions().position(latLon).title(location))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 10.0f))
        onMapClicked(latLon)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}