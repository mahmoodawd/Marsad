package com.example.marsad.data.network

data class WeatherResponse(
    val city: City? = null,
    val cnt: Int? = null,
    val cod: String? = null,
    val message: Int? = null,
    val list: List<ListItem?>? = null
)

data class City(
    val country: String? = null,
    val coord: Coord? = null,
    val sunrise: Int? = null,
    val timezone: Int? = null,
    val sunset: Int? = null,
    val name: String? = null,
    val id: Int? = null,
    val population: Int? = null
)

data class Coord(
	val lon: Any? = null,
	val lat: Any? = null
)

data class ListItem(
	val dt: Int? = null,
	val pop: Int? = null,
	val visibility: Int? = null,
	val dtTxt: String? = null,
	val weather: List<WeatherItem?>? = null,
	val main: Main? = null,
	val clouds: Clouds? = null,
	val sys: Sys? = null,
	val wind: Wind? = null,
	val rain: Rain? = null
)

data class WeatherItem(
	val icon: String? = null,
	val description: String? = null,
	val main: String? = null,
	val id: Int? = null
)

data class Main(
	val temp: Any? = null,
	val tempMin: Any? = null,
	val grndLevel: Int? = null,
	val tempKf: Any? = null,
	val humidity: Int? = null,
	val pressure: Int? = null,
	val seaLevel: Int? = null,
	val feelsLike: Any? = null,
	val tempMax: Any? = null
)

data class Clouds(
	val all: Int? = null
)

data class Sys(
    val pod: String? = null
)

data class Wind(
    val deg: Int? = null,
    val speed: Any? = null,
    val gust: Any? = null
)

data class Rain(
	val jsonMember3h: Any? = null
)

