package com.example.marsad.data.model

object AlertType {
    const val NOTIFICATION = "notification"
    const val ALARM = "alarm"
}

data class AlertItem(val startTime: Long, val endTime: Long, val alertType: String)