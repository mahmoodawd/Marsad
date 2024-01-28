package com.example.marsad.data.database.typeconverters

import androidx.room.TypeConverter
import com.example.marsad.data.database.entities.WeatherEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherConverter {
    @TypeConverter
    fun fromJson(value: String): List<WeatherEntity> {
        val listType = object : TypeToken<List<WeatherEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<WeatherEntity>): String {
        return Gson().toJson(list)
    }
}