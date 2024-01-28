package com.example.marsad.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "CLOUDS")
data class CloudsEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("clouds_id") val id: Int,
    @ColumnInfo("all") var all: Int,

    )