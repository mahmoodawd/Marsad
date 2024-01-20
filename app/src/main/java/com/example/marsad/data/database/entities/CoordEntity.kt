package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("COORD")
data class CoordEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("coord_id") val id: Int,
    @ColumnInfo("lat") var lat: Double,
    @ColumnInfo("lon") var lon: Double,

    )