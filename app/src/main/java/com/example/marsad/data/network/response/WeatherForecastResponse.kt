package com.example.marsad.data.network.response

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var timeStamps: ArrayList<TimeStamp> = arrayListOf(),
    @SerializedName("city") var city: City? = City(),

    )