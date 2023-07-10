package com.example.marsad.data.repositories

import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.AlertsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakeAlertsRepository : AlertsRepositoryInterface {
    private val flow = MutableSharedFlow<AlertResponse>()

    suspend fun emit(value: AlertResponse) = flow.emit(value)

    override fun getActiveAlerts(): Flow<List<AlertItem>> {
        return flow { emit(storedAlerts) }
    }

    override fun getAlertById(id: Long): Flow<AlertItem> {
        return flow {
            storedAlerts.find {
                it.id == id
            }?.let { emit(it) }
        }
    }

    override suspend fun addNewAlert(alertItem: AlertItem): Long {
        storedAlerts.add(alertItem)
        return storedAlerts.size.toLong()
    }

    override suspend fun deleteAlert(alertItem: AlertItem): Int {
        storedAlerts.remove(alertItem)
        return storedAlerts.size
    }

    override suspend fun getWeatherAlerts(lat: Double, lon: Double): Flow<AlertResponse> = flow


    companion object {
        private val currentTime = System.currentTimeMillis()
        val passedAlerts = listOf(
            AlertItem(1, AlertType.ALARM, currentTime - 3600000, currentTime + 3600000),
            AlertItem(2, AlertType.NOTIFICATION, currentTime - 7200000, currentTime + 3600000),
        )
        val activeAlerts = listOf(
            AlertItem(3, AlertType.NOTIFICATION, currentTime + 7200000, currentTime + 10800000),
            AlertItem(4, AlertType.ALARM, currentTime + 10800000, currentTime + 14400000),
        )

        var storedAlerts = (passedAlerts + activeAlerts) as MutableList

        val alert1 = AlertsItem(
            start = 1654320000, // 2022-06-04T00:00:00Z
            description = "Heavy rain expected in the area",
            senderName = "National Weather Service",
            end = 1654395600, // 2022-06-04T20:00:00Z
            event = "Rain",
            tags = listOf("weather", "alert")
        )

        val alert2 = AlertsItem(
            start = 1654478400, // 2022-06-05T00:00:00Z
            description = "High winds expected in the area",
            senderName = null,
            end = 1654554000, // 2022-06-05T20:00:00Z
            event = "Windy",
            tags = listOf("weather", "alert")
        )

        val alertResponse = AlertResponse(
            alerts = listOf(),
            lat = 37.7749,
            lon = -122.4194,
            timezone = "America/Los_Angeles",
            timezone_offset = -25200
        )
    }
}
