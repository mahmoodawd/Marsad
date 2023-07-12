package com.example.marsad.data.model

import androidx.room.TypeConverter
import com.example.marsad.data.network.DailyWeather
import com.example.marsad.data.network.HourlyWeather
import com.example.marsad.data.network.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyWeatherListConverter {
    @TypeConverter
    fun fromJson(value: String): List<HourlyWeather> {
        val listType = object : TypeToken<List<HourlyWeather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<HourlyWeather>): String {
        return Gson().toJson(list)
    }
}

class DailyWeatherListConverter {
    @TypeConverter
    fun fromJson(value: String): List<DailyWeather> {
        val listType = object : TypeToken<List<DailyWeather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<DailyWeather>): String {
        return Gson().toJson(list)
    }
}
class WeatherListConverter {
    @TypeConverter
    fun fromJson(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Weather>): String {
        return Gson().toJson(list)
    }
}