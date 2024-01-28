package com.example.marsad.ui.weatheralerts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.network.Resource
import com.example.marsad.data.network.asResource
import com.example.marsad.domain.models.Alert
import com.example.marsad.domain.models.NotificationAlert
import com.example.marsad.domain.repositories.AlertsRepositoryInterface
import com.example.marsad.ui.weatheralerts.view.AlertsUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherAlertsViewModel(
    private val repository: AlertsRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {


    val alertsUiState: StateFlow<AlertsUiState> =
        repository.getActiveAlerts().asResource()
            .map { resource ->
                when (resource) {
                    is Resource.Failure -> AlertsUiState.Error
                    Resource.Loading -> AlertsUiState.Loading
                    is Resource.Success -> {
                        val alerts = (resource.data)
                        if (alerts.isEmpty()) AlertsUiState.Empty else AlertsUiState.Success(alerts)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = AlertsUiState.Loading,
                started = SharingStarted.WhileSubscribed(5000)
            )


    private val _weatherAlerts: MutableStateFlow<MutableList<NotificationAlert>> =
        MutableStateFlow(mutableListOf())
    val weatherAlerts: StateFlow<List<NotificationAlert>> = _weatherAlerts.asStateFlow()


    fun addAlert(alert: Alert) =
        viewModelScope.launch(dispatcher) {
            repository.addNewAlert(alert)
            checkForAlerts(alert)
        }

    private fun checkForAlerts(alert: Alert) {
        viewModelScope.launch {
            repository.checkForAlerts(alert.lat, alert.lon).asResource().map { resource ->
                when (resource) {
                    is Resource.Failure -> {
                        Log.e(TAG, "checkForAlerts failed: ${resource.error?.message}")
                    }

                    Resource.Loading -> {}

                    is Resource.Success<*> -> {
                        val notificationAlert =
                            resource.data as NotificationAlert

                        if (notificationAlert.isMatchingWith(0, 0)) {
                            _weatherAlerts.value.add(
                                notificationAlert.copy(id = alert.id, type = alert.type)
                            )
                        } else {
                            Log.i(TAG, "checkForAlerts: NO matching Alerts")
                        }

                    }
                }
            }
        }
    }


    fun removeAlert(alert: Alert) =
        viewModelScope.launch(dispatcher) {
            repository.deleteAlert(alert)
        }

    companion object {
        private val TAG = WeatherAlertsViewModel::class.java.simpleName
    }
}