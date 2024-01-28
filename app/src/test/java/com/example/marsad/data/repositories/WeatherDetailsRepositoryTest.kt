package com.example.marsad.data.repositories

import com.example.marsad.data.database.localdatasources.FakeWeatherLocalDataSource
import com.example.marsad.data.mappers.toWeatherDetails
import com.example.marsad.data.mappers.toWeatherForecastEntity
import com.example.marsad.data.network.remotesources.FakeWeatherRemoteDataSource
import com.example.marsad.domain.datasources.WeatherDetailsLocalDataSourceInterface
import com.example.marsad.domain.datasources.WeatherDetailsRemoteSourceInterface
import com.example.marsad.domain.repositories.WeatherDetailsRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherDetailsRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var subject: WeatherDetailsRepositoryInterface
    private lateinit var localSource: WeatherDetailsLocalDataSourceInterface
    private lateinit var remoteSource: WeatherDetailsRemoteSourceInterface

    @Before
    fun setup() {
        localSource = FakeWeatherLocalDataSource()
        remoteSource = FakeWeatherRemoteDataSource()

        subject = WeatherDetailsRepository.getInstance(remoteSource, localSource)

    }


    @Test
    fun weatherDetailsRepository_weather_details_stream_is_backed_by_local_source() {
        testScope.runTest {
            assertEquals(
                localSource.getWeatherForecast(0.0, 0.0, "en").first()?.toWeatherDetails(),
                subject.getWeatherDetails(0.0, 0.0, "en").first()
            )
        }
    }

    @Test
    fun weatherDetailsRepository_sync_pulls_from_remote_source() {
        testScope.runTest {
            subject.syncWeatherDetails(0.0, 0.0, "en")
            val detailsFromRemote =
                remoteSource.getWeatherForecast(0.0, 0.0, "en")
                    .toWeatherForecastEntity("en")
                    .toWeatherDetails()
            val detailsFromLocal =
                localSource.getWeatherForecast(0.0, 0.0, "en").first()?.toWeatherDetails()

            assertEquals(detailsFromLocal, detailsFromRemote)
        }
    }
}