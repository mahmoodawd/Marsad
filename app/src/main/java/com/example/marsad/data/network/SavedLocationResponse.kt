package com.example.marsad.data.network

data class OpenWeatherMapResponse(
    val coord: Coord,
    val main: Main,
    val weather: List<WeatherInfo>
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Main(
    val temp: Double
)

data class WeatherInfo(
    val icon: String,
    val description: String
)