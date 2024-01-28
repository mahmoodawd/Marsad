package com.example.marsad.data.network.remotesources

import com.example.marsad.data.network.response.City
import com.example.marsad.data.network.response.Clouds
import com.example.marsad.data.network.response.Coord
import com.example.marsad.data.network.response.CurrentWeatherResponse
import com.example.marsad.data.network.response.Main
import com.example.marsad.data.network.response.OneCallWeatherResponse
import com.example.marsad.data.network.response.TimeStamp
import com.example.marsad.data.network.response.Weather
import com.example.marsad.data.network.response.WeatherForecastResponse
import com.example.marsad.data.network.response.Wind
import com.example.marsad.domain.datasources.WeatherDetailsRemoteSourceInterface
import retrofit2.Response

class FakeWeatherRemoteDataSource : WeatherDetailsRemoteSourceInterface {
    private val fakeCity = City(
        name = "FakeCity",
        coord = Coord(0.0, 0.00),
        country = "",
        population = 0,
        timezone = 0,
        sunrise = 0,
        sunset = 0
    )
    private val fakeMain = Main(0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0)
    private val fakeClouds = Clouds(0)
    private val fakeWind = Wind(0.0, 0, 0.0)
    private val fakeWeather = Weather(0, "", "", "")
    private val fakeTimeStamp =
        TimeStamp(0, fakeMain, arrayListOf(fakeWeather), fakeClouds, fakeWind, 0, dtTxt = "")

    private val forecastResponse = WeatherForecastResponse(
        city = fakeCity,
        timeStamps = arrayListOf(fakeTimeStamp)
    )

    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String,
    ): WeatherForecastResponse = forecastResponse

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
    ): Response<CurrentWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherAlerts(lat: Double, lon: Double): OneCallWeatherResponse {
        TODO("Not yet implemented")
    }
}