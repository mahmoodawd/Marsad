package com.example.marsad.data.mappers

import com.example.marsad.data.database.entities.CityEntity
import com.example.marsad.data.database.entities.CloudsEntity
import com.example.marsad.data.database.entities.CoordEntity
import com.example.marsad.data.database.entities.MainEntity
import com.example.marsad.data.database.entities.TimeStampEntity
import com.example.marsad.data.database.entities.WeatherEntity
import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.data.database.entities.WindEntity
import com.example.marsad.data.network.response.WeatherForecastResponse
import com.example.marsad.data.network.response.City as CityResponse
import com.example.marsad.data.network.response.Clouds as CloudsResponse
import com.example.marsad.data.network.response.Coord as CoordResponse
import com.example.marsad.data.network.response.Main as MainResponse
import com.example.marsad.data.network.response.TimeStamp as TimeStampResponse
import com.example.marsad.data.network.response.Weather as WeatherResponse
import com.example.marsad.data.network.response.Wind as WindResponse


fun WeatherForecastResponse.toWeatherForecastEntity(language: String): WeatherForecastEntity =
    WeatherForecastEntity(
        lat = city?.coord?.lat ?: 0.0,
        lon = city?.coord?.lon ?: 0.0,
        language = language,
        timeStamps = timeStamps.map { it.toTimeStampEntity() },
        city = city!!.toCityEntity()
    )


private fun TimeStampResponse.toTimeStampEntity(): TimeStampEntity = TimeStampEntity(

    dt = dt!!,
    visibility = visibility!!,
    dtTxt = dtTxt!!,
    weather = weather.map { it.toWeatherEntity() },
    main = main!!.toMainEntity(),
    clouds = clouds!!.toCloudsEntity(),
    wind = wind!!.toWindEntity(),
)

private fun WeatherResponse.toWeatherEntity(): WeatherEntity = WeatherEntity(
    main = main!!,
    description = description!!,
    icon = icon!!,
)

private fun MainResponse.toMainEntity(): MainEntity = MainEntity(
    grndLevel = grndLevel!!,
    seaLevel = seaLevel!!,
    temp = temp!!,
    feelsLike = feelsLike!!,
    tempMin = tempMin!!,
    tempMax = tempMax!!,
    pressure = pressure!!,
    humidity = humidity!!
)

private fun CloudsResponse.toCloudsEntity(): CloudsEntity = CloudsEntity(
    all = all!!
)

private fun WindResponse.toWindEntity(): WindEntity = WindEntity(
    speed = speed!!,
    deg = deg!!,
    gust = gust!!,
)

private fun CityResponse.toCityEntity(): CityEntity = CityEntity(
    coord = coord?.toCoordEntity() ?: CoordEntity(lat = 0.0, lon = 0.0),
    population = population!!,
    name = name!!,
    country = country!!,
    timezone = timezone!!,
    sunrise = sunrise!!,
    sunset = sunset!!
)

private fun CoordResponse.toCoordEntity(): CoordEntity = CoordEntity(
    lat = lat ?: 0.0,
    lon = lon ?: 0.0
)
