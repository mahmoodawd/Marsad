package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity("CITY")
data class CityEntity(
    @ColumnInfo("name") var name: String,
    @Embedded("coord_") var coord: CoordEntity,
    @ColumnInfo("country") var country: String,
    @ColumnInfo("population") var population: Int,
    @ColumnInfo("timezone") var timezone: Int,
    @ColumnInfo("sunrise") var sunrise: Int,
    @ColumnInfo("sunset") var sunset: Int,
)