package com.example.marsad.data.network.response

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class OneCallWeatherResponse(
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_offset") var timezoneOffset: Int? = null,
    @SerializedName("current") var current: Current? = Current(),
    @SerializedName("minutely") var minutely: ArrayList<Minutely> = arrayListOf(),
    @SerializedName("daily") var daily: ArrayList<Daily> = arrayListOf(),
    @SerializedName("hourly") var hourly: ArrayList<Hourly> = arrayListOf(),
    @SerializedName("alerts") var alerts: ArrayList<Alert> = arrayListOf(),
)