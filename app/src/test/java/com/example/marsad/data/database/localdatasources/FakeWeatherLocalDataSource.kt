package com.example.marsad.data.database.localdatasources

import com.example.marsad.data.database.entities.CityEntity
import com.example.marsad.data.database.entities.CloudsEntity
import com.example.marsad.data.database.entities.CoordEntity
import com.example.marsad.data.database.entities.MainEntity
import com.example.marsad.data.database.entities.TimeStampEntity
import com.example.marsad.data.database.entities.WeatherEntity
import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.data.database.entities.WindEntity
import com.example.marsad.data.network.response.OneCallWeatherResponse
import com.example.marsad.domain.datasources.WeatherDetailsLocalDataSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWeatherLocalDataSource : WeatherDetailsLocalDataSourceInterface {

    private val fakeCity = CityEntity("FakeCity", CoordEntity(0.0, 0.00), "", 0, 0, 0, 0)
    private val fakeMain = MainEntity(0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0)
    private val fakeClouds = CloudsEntity(0)
    private val fakeWind = WindEntity(0.0, 0, 0.0)
    private val fakeWeather = WeatherEntity("", "", "")
    private val fakeTimeStamp =
        TimeStampEntity(0, fakeMain, listOf(fakeWeather), fakeClouds, fakeWind, 0, "")

    private val weatherForecastStateFlow: MutableStateFlow<WeatherForecastEntity?> =
        MutableStateFlow(
            WeatherForecastEntity(
                lat = 42.52,
                lon = 75.23,
                language = "en",
                city = fakeCity,
                timeStamps = listOf(fakeTimeStamp)
            )

        )

    override fun <T> getAllItems(): Flow<List<T>> {
        TODO("Not yet implemented")
    }

    override fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String,
    ): Flow<WeatherForecastEntity?> =
        weatherForecastStateFlow


    override suspend fun addNewItem(item: OneCallWeatherResponse): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateWeatherDetails(weatherForecastEntity: WeatherForecastEntity): Long {
        weatherForecastStateFlow.update { weatherForecastEntity }
        return 1
    }

    override suspend fun deleteItem(item: OneCallWeatherResponse): Int {
        TODO("Not yet implemented")
    }
}