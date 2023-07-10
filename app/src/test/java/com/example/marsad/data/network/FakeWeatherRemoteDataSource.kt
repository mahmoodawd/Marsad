package com.example.marsad.data.network

import retrofit2.Response

class FakeWeatherRemoteDataSource : RemoteSource {
    lateinit var fakeOneCallResponse: OneCallResponse
    lateinit var fakeOpenWeatherMapResponse: OpenWeatherMapResponse
    override suspend fun getWeatherStatus(lat: Double, lon: Double): Response<OneCallResponse> {
        fakeOneCallResponse = OneCallResponse(
            lat = 33.44,
            lon = -94.04,
            timezone = "America/Chicago",
            timezone_offset = -18000,
            current = CurrentWeather(
                dt = 1625881590,
                sunrise = 1625851175,
                sunset = 1625903620,
                temp = 298.76,
                feels_like = 299.26,
                pressure = 1012,
                humidity = 70,
                dew_point = 292.59,
                uvi = 11.27,
                clouds = 90,
                visibility = 10000,
                wind_speed = 3.09,
                wind_gust = null,
                wind_deg = 259,
                weather = listOf(
                    Weather(
                        id = 804,
                        main = "Clouds",
                        description = "overcast clouds",
                        icon = "04n"
                    )
                )
            ),
            hourly = listOf(),
            daily = listOf(),
        )
        return Response.success(fakeOneCallResponse)
    }

    override suspend fun getLocationWeather(
        lat: Double,
        lon: Double
    ): Response<OpenWeatherMapResponse> {
        fakeOpenWeatherMapResponse = OpenWeatherMapResponse(
            coord = Coord(lat = 37.7749, lon = -122.4194),
            main = Main(temp = 290.65),
            weather = listOf(
                WeatherInfo(icon = "01d", description = "clear sky")
            )
        )
        return Response.success(fakeOpenWeatherMapResponse)
    }

    override suspend fun getWeatherAlerts(lat: Double, lon: Double): Response<AlertResponse> {
        TODO("Not yet implemented")
    }
}