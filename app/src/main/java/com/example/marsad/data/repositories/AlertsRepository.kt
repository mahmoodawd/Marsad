package com.example.marsad.data.repositories

import com.example.marsad.data.database.entities.AlertEntity
import com.example.marsad.data.mappers.toAlert
import com.example.marsad.data.mappers.toAlertEntity
import com.example.marsad.domain.datasources.AlertLocalDataSourceInterface
import com.example.marsad.domain.datasources.WeatherDetailsRemoteSourceInterface
import com.example.marsad.domain.models.Alert
import com.example.marsad.domain.models.NotificationAlert
import com.example.marsad.domain.repositories.AlertsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class AlertsRepository private constructor(
    private val remoteSource: WeatherDetailsRemoteSourceInterface,
    private val localSource: AlertLocalDataSourceInterface,
) : AlertsRepositoryInterface {

    companion object {
        private val TAG = AlertsRepository::class.java.simpleName

        private var instance: AlertsRepository? = null
        fun getInstance(
            remoteSource: WeatherDetailsRemoteSourceInterface,
            localSource: AlertLocalDataSourceInterface,
        ): AlertsRepository {
            if (instance == null) {
                instance = AlertsRepository(remoteSource, localSource)
            }
            return instance as AlertsRepository
        }
    }

    override fun getActiveAlerts(): Flow<List<Alert>> =
        localSource.getAllItems().map { it.map(AlertEntity::toAlert) }.flowOn(Dispatchers.IO)

    override suspend fun addNewAlert(alertItem: Alert): Long =
        localSource.addNewItem(alertItem.toAlertEntity())

    override suspend fun deleteAlert(alertItem: Alert): Int =
        localSource.deleteItem(alertItem.toAlertEntity())


    override fun getAlertById(id: Long): Flow<Alert> {
        TODO("Not yet implemented")
    }

    override suspend fun checkForAlerts(lat: Double, lon: Double): Flow<NotificationAlert> = flowOf(
//             alertsResponse.takeIf { it.isNotEmpty() }?.first()?.toNotificationAlert()
        NotificationAlert(
            title = "Soon",
            description = "This Feature is not working at the current time",
            firesAt = 0,
            endsAt = 0,
        )
    ).flowOn(Dispatchers.IO)
}