package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "WIND")
data class WindEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("wind_id") val id: Int,
    @ColumnInfo("speed") var speed: Double,
    @ColumnInfo("deg") var deg: Int,
    @ColumnInfo("gust") var gust: Double,

    )