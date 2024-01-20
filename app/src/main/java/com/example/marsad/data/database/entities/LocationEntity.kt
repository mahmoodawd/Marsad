package com.example.marsad.data.database.entities

import androidx.room.Entity

@Entity(tableName = "saved_locations", primaryKeys = ["lat", "lon"])
data class LocationEntity(
    val lat: Double,
    val lon: Double,
    val city: String,
    val country: String,
    val lastTemp: Int,
    val icon: String,
    val description: String,
)
