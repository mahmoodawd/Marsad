package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.marsad.data.database.typeconverters.TimeStampConverter


@Entity(tableName = "WEATHER_DETAILS", primaryKeys = ["name", "coord_lat", "coord_lon"])

data class WeatherForecastEntity(
    @ColumnInfo("latitude") val lat: Double,
    @ColumnInfo("longitude") val lon: Double,
    @ColumnInfo("language") val language: String,
    @TypeConverters(TimeStampConverter::class)
    val timeStamps: List<TimeStampEntity>,
    @Embedded val city: CityEntity,
)
