package com.example.marsad.data.mappers

import com.example.marsad.core.utils.getTimeFromOffset
import com.example.marsad.data.database.entities.CityEntity
import com.example.marsad.data.database.entities.MainEntity
import com.example.marsad.data.database.entities.TimeStampEntity
import com.example.marsad.data.database.entities.WeatherEntity
import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.domain.models.Daily
import com.example.marsad.domain.models.Hourly
import com.example.marsad.domain.models.WeatherDetails
import com.example.marsad.domain.models.City as CityModel
import com.example.marsad.domain.models.Main as MainModel
import com.example.marsad.domain.models.TimeStamp as TimeStampModel
import com.example.marsad.domain.models.Weather as WeatherModel

private const val WEATHER_ICON_BASE_URL = "https://openweathermap.org/img/wn/"
private const val SMALL_ICON_SIZE = "@2x.png"
private const val BIG_ICON_SIZE = "@4x.png"
fun WeatherForecastEntity.toWeatherDetails(): WeatherDetails =
    WeatherDetails(
        dateTime = getTimeFromOffset(city.timezone),
        daily = timeStamps.chunked(8).map { it.first().toDailyModel() },
        hourly = timeStamps.take(8).map { it.toHourlyModel() },
        current = timeStamps.first().toTimeStampModel(),
        city = city.toCityModel()
    )

private fun TimeStampEntity.toDailyModel(): Daily = Daily(
    id = 0,
    description = weather.first().description,
    icon = WEATHER_ICON_BASE_URL.plus(weather.first().icon).plus(SMALL_ICON_SIZE),
    date = dt,
    tempMin = main.tempMin.toInt(),
    tempMax = main.tempMax.toInt()
)

private fun TimeStampEntity.toHourlyModel(): Hourly = Hourly(
    id = 0,
    hour = dt,
    icon = WEATHER_ICON_BASE_URL.plus(weather.first().icon).plus(SMALL_ICON_SIZE),
    temp = main.temp.toInt()
)

private fun TimeStampEntity.toTimeStampModel(): TimeStampModel = TimeStampModel(
    dateTime = dtTxt,
    weather = weather[0].toWeatherMode(),
    main = main.toMainModel(),
    clouds = clouds.all,
    windSpeed = wind.speed,
)

private fun WeatherEntity.toWeatherMode(): WeatherModel = WeatherModel(
    description = description,
    icon = WEATHER_ICON_BASE_URL.plus(icon).plus(BIG_ICON_SIZE),
)

private fun MainEntity.toMainModel(): MainModel = MainModel(
    temperature = temp.toInt(),
    feelsLike = feelsLike.toInt(),
    tempMin = tempMin.toInt(),
    tempMax = tempMax.toInt(),
    pressure = pressure,
    humidity = humidity
)

private fun CityEntity.toCityModel(): CityModel = CityModel(
    name = name,
    country = country,
    timezone = timezone,
    sunrise = sunrise,
    sunset = sunset
)



