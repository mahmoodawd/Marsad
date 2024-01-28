package com.example.marsad.domain.models


data class WeatherDetails(
    val dateTime: String,
    val city: City,
    val current: TimeStamp,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)


data class City(
    val name: String,
    val country: String,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int,
)

data class TimeStamp(
    val dateTime: String,
    val main: Main,
    val weather: Weather,
    val clouds: Int,
    val windSpeed: Double,
)

data class Main(
    val temperature: Int,
    val feelsLike: Int,
    val tempMin: Int,
    val tempMax: Int,
    val pressure: Int,
    val humidity: Int,
)

data class Weather(
    val description: String,
    val icon: String,
)

data class Daily(
    val id: Int,
    val description: String,
    val icon: String,
    val date: Int,
    val tempMin: Int,
    val tempMax: Int,
)

data class Hourly(
    val id: Int,
    val hour: Int,
    val icon: String,
    val temp: Int,
)


