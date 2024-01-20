package com.example.marsad.data.network.response

import com.google.gson.annotations.SerializedName


data class Rain(

    @SerializedName("1h") var volume: Double? = null,

    )