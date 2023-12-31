package com.example.marsad.ui.weatheralerts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.repositories.FakeAlertsRepository
import com.example.marsad.getOrAwaitValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherAlertsViewModelTest {
    private lateinit var alertsViewModel: WeatherAlertsViewModel
    private lateinit var fakeRepository: FakeAlertsRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        fakeRepository = FakeAlertsRepository()
        alertsViewModel = WeatherAlertsViewModel(fakeRepository)
    }

    @Test
    fun getActiveAlerts_localActiveAlerts() {
        //When requesting active alerts
        val value = alertsViewModel.getActiveAlerts().getOrAwaitValue()
        //Then assert that active tasks retrieved
        assertThat(value, not(nullValue()))
    }

    @Test
    fun getAlertById_alertId_desiredAlertItem() = runTest {
        //When calling getAlertById
        val value =
            alertsViewModel.getAlertById(FakeAlertsRepository.storedAlerts[0].id).getOrAwaitValue()
        advanceUntilIdle()
        //Then Assert that the returned alert not null
        assertThat(value, `is`(FakeAlertsRepository.storedAlerts[0]))
    }

    @Test
    fun addAlert_alertItem_alertInserted() = runTest {
        //Given New Alert Item
        val alert = AlertItem(8, AlertType.NOTIFICATION, 369000, 258000)
        val oldSize = FakeAlertsRepository.storedAlerts.size
        //When calling addAlert
        alertsViewModel.addAlert(alert)
        advanceUntilIdle()
        //Then Alert Should be Added
        assertEquals(FakeAlertsRepository.storedAlerts.size, oldSize + 1)
    }

    @Ignore("Conflict With Add")
    @Test
    fun removeAlert_alertItem_alertRemoved() = runTest {
        //Given Existing Alert Item
        val oldSize = FakeAlertsRepository.storedAlerts.size
        val alert = FakeAlertsRepository.storedAlerts[2]
        //When calling getAlertById
        alertsViewModel.removeAlert(alert)
        advanceUntilIdle()
        //Item Should be Deleted and list decreased
        assertEquals(FakeAlertsRepository.storedAlerts.size, oldSize - 1)
    }

    @Test
    fun getWeatherAlerts_locationLatAndLon_AlertsResponse() {
        runTest {
            assertEquals(ApiState.Loading, alertsViewModel.weatherAlertsStateFlow.value)
            alertsViewModel.getWeatherAlerts(
                lat = 0.0,
                lon = 1.0
            )

            val alertResponse = AlertResponse(
                alerts = listOf(),
                lat = 37.7749,
                lon = -122.4194,
                timezone = "America/Los_Angeles",
                timezone_offset = -25200
            )
            fakeRepository.emit(alertResponse)
            assertThat(
                alertsViewModel.weatherAlertsStateFlow.value, `is`
                    (ApiState.Success(alertResponse))
            )
        }
    }

}
