package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.marsad.data.database.typeconverters.WeatherConverter

@Entity(tableName = "TIME_STAMPS")
data class TimeStampEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("time_stamp_id") val id: Int,
    @ColumnInfo("dt")
    var dt: Int,
    @Embedded
    var main: MainEntity,
    @TypeConverters(WeatherConverter::class)
    var weather: List<WeatherEntity> = listOf(),
    @Embedded
    var clouds: CloudsEntity,
    @Embedded
    var wind: WindEntity,
    @ColumnInfo("visibility")
    var visibility: Int,
    @ColumnInfo("dt_txt")
    var dtTxt: String,

    )