package com.example.marsad.data.network

data class AlertResponse(
    val alerts: List<AlertsItem>?,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
)

data class AlertsItem(
    val start: Long,
    val description: String,
    val senderName: String?,
    val end: Long,
    val event: String,
    val tags: List<String>
)

