package com.example.marsad.domain.models

data class Alert(
    val id: Int = 0,
    val startTime: Int,
    val endTime: Int,
    val lat: Double,
    val lon: Double,
    val city: String,
    val type: String,
)
