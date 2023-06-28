package com.example.marsad.data.network

data class OneCallResponse(
	val lat: Double,
	val lon: Double,
	val timezone: String,
	val timezone_offset: Long,
	val current: CurrentWeather,
	val hourly: List<HourlyWeather>,
	val daily: List<DailyWeather>
)

data class CurrentWeather(
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
	val weather: List<Weather>
)

data class HourlyWeather(
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
	val weather: List<Weather>,
	val pop: Double
)

data class DailyWeather(
	val dt: Long,
	val sunrise: Long,
	val sunset: Long,
	val moonrise: Long,
	val moonset: Long,
	val moon_phase: Double,
	val temp: Temperature,
	val feels_like: FeelsLikeTemperature,
	val pressure: Int,
	val humidity: Int,
	val dew_point: Double,
	val wind_speed: Double,
	val wind_deg: Int,
	val wind_gust: Double,
	val weather: List<Weather>,
	val clouds: Int,
	val pop: Double,
	val rain: Double?,
	val uvi: Double
)

data class Temperature(
	val day: Double,
	val min: Double,
	val max: Double,
	val night: Double,
	val eve: Double,
	val morn: Double
)

data class FeelsLikeTemperature(
	val day: Double,
	val night: Double,
	val eve: Double,
	val morn: Double
)

data class Weather(
	val id: Int,
	val main: String,
	val description: String,
	val icon: String
)