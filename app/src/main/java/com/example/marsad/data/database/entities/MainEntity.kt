package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "MAIN")
data class MainEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("main_id") val id: Int,
    @ColumnInfo("temp") var temp: Double,
    @ColumnInfo("feels_like") var feelsLike: Double,
    @ColumnInfo("temp_min") var tempMin: Double,
    @ColumnInfo("temp_max") var tempMax: Double,
    @ColumnInfo("pressure") var pressure: Int,
    @ColumnInfo("sea_level") var seaLevel: Int,
    @ColumnInfo("grnd_level") var grndLevel: Int,
    @ColumnInfo("humidity") var humidity: Int,
)