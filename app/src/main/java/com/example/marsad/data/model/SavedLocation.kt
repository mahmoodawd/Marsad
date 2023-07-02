package com.example.marsad.data.model

import android.os.Parcelable
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "savedLocation", primaryKeys = ["lat", "lon"])
data class SavedLocation(
    val city: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val lastTemp: Int = 0,
    val icon: String = "",
    val description: String = ""
) : Serializable
