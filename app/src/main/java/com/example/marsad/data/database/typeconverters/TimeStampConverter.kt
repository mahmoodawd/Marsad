package com.example.marsad.data.database.typeconverters

import androidx.room.TypeConverter
import com.example.marsad.data.database.entities.TimeStampEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TimeStampConverter {
    @TypeConverter
    fun fromJson(value: String): List<TimeStampEntity> {
        val listType = object : TypeToken<List<TimeStampEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<TimeStampEntity>): String {
        return Gson().toJson(list)
    }
}