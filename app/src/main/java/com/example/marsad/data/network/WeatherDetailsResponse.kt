package com.example.marsad.data.network

import androidx.room.*
import com.example.marsad.data.model.DailyWeatherListConverter
import com.example.marsad.data.model.HourlyWeatherListConverter
import com.example.marsad.data.model.WeatherListConverter

@Entity(tableName = "weather_details_response", primaryKeys = ["lat", "lon"])
data class WeatherDetailsResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
    @Embedded val current: CurrentWeather,
    @TypeConverters(HourlyWeatherListConverter::class)
    val hourly: List<HourlyWeather>,
    @TypeConverters(DailyWeatherListConverter::class)
    val daily: List<DailyWeather>
)

@Entity
data class CurrentWeather(
    @PrimaryKey
    @ColumnInfo(name = "current_weather_id")
    val id: Int?,
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    @TypeConverters(WeatherListConverter::class)
    val weather: List<Weather>
)

@Entity
data class HourlyWeather(
    @PrimaryKey(autoGenerate = true)
    val hourId: Int?,
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    @TypeConverters(WeatherListConverter::class)
    val weather: List<Weather>,
    val pop: Double
)

@Entity
data class DailyWeather(
    @PrimaryKey(autoGenerate = true)
    val dayId: Int?,
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Double,
    @Embedded val temp: Temperature,
    @Embedded val feels_like: FeelsLikeTemperature,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    @TypeConverters(WeatherListConverter::class)
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double?,
    val uvi: Double
)

@Entity(tableName = "temperature")
data class Temperature(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "temperature_id")
    val temperatureId: Int?,
    @ColumnInfo(name = "day_temp")
    val day: Double,
    @ColumnInfo(name = "min_temp")
    val min: Double,
    @ColumnInfo(name = "max_temp")
    val max: Double,
    @ColumnInfo(name = "night_temp")
    val night: Double,
    @ColumnInfo(name = "eve_temp")
    val eve: Double,
    @ColumnInfo(name = "morn_temp")
    val morn: Double
)

@Entity(tableName = "feels_like_temperature")
data class FeelsLikeTemperature(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "feels_like_id")
    val feelsLikeId: Int?,
    @ColumnInfo(name = "day_feels_like")
    val day: Double,
    @ColumnInfo(name = "night_feels_like")
    val night: Double,
    @ColumnInfo(name = "eve_feels_like")
    val eve: Double,
    @ColumnInfo(name = "morn_feels_like")
    val morn: Double
)

@Entity
data class Weather(
    @PrimaryKey
    val id: Int?,
    val main: String,
    val description: String,
    val icon: String
)