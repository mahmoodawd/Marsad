package com.example.marsad.data.model

import androidx.room.Entity

object AlertType {
    const val NOTIFICATION = "notification"
    const val ALARM = "alarm"
}

@Entity(tableName = "alert", primaryKeys = ["startTime", "endTime"])
data class AlertItem(var startTime: Long, var endTime: Long, var alertType: String)