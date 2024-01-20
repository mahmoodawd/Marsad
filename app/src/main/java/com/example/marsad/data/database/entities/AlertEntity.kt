package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_alerts")
data class AlertEntity(
//    @ColumnInfo("sender_name") var senderName: String,
//    @ColumnInfo("event") var event: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("alert_id") val id: Int,
    @ColumnInfo("latitude") val lat: Double,
    @ColumnInfo("longitude") val lon: Double,
    @ColumnInfo("city") val city: String,
    @ColumnInfo("start") var start: Int,
    @ColumnInfo("end") var end: Int,
    @ColumnInfo("type") val type: String,
//    @ColumnInfo("description") var description: String,
//    @ColumnInfo("tags") var tags: ArrayList<String>,
)
