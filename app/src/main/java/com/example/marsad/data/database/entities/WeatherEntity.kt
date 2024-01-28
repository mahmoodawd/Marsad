package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "WEATHER")

data class WeatherEntity(
//    @PrimaryKey
//    @ColumnInfo("weather_id") var id: Int,
    @ColumnInfo("main") var main: String,
    @ColumnInfo("description") var description: String,
    @ColumnInfo("icon") var icon: String,

    )