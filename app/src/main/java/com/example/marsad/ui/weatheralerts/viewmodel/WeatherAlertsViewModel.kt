package com.example.marsad.ui.weatheralerts.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.repositories.AlertsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.DigestInputStream

class WeatherAlertsViewModel(private val repository: AlertsRepositoryInterface) : ViewModel() {
    private val TAG = WeatherAlertsViewModel::class.java.simpleName
    private val _weatherAlertsStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val weatherAlertsStateFlow: StateFlow<ApiState> = _weatherAlertsStateFlow.asStateFlow()

    fun getActiveAlerts(): MutableLiveData<List<AlertItem>> {
        val alertList = MutableLiveData<List<AlertItem>>()
        viewModelScope.launch {
            repository.getActiveAlerts().catch { e ->
                Log.i(TAG, "getActiveAlerts: ${e.message}")
            }.collect {
                alertList.postValue(it.filter { item ->
                    item.start > System.currentTimeMillis()
                })
                it.filter { item ->
                    item.start <= System.currentTimeMillis()
                }.forEach { passedItem ->
                    repository.deleteAlert(passedItem)
                }
            }
        }
        return alertList
    }

    fun getAlertById(alertId: Long): MutableLiveData<AlertItem> {
        val alertItem = MutableLiveData<AlertItem>()
        viewModelScope.launch {
            repository.getAlertById(alertId).catch { e ->
                Log.i(TAG, "getAlertById: ${e.message}")
            }.collect {
                alertItem.value = it
            }
        }
        return alertItem
    }

    fun addAlert(alert: AlertItem): MutableLiveData<Boolean> {
        val status = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val st = repository.addNewAlert(alert)
            withContext(Dispatchers.Main) {
                status.value = st > 0
            }
        }
        return status
    }

    fun removeAlert(alert: AlertItem): MutableLiveData<Boolean> {
        val status = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val st = repository.deleteAlert(alert)
            withContext(Dispatchers.Main) {

                status.value = st > 0
            }
        }
        return status
    }

    fun getWeatherAlerts(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getWeatherAlerts(lat, lon).catch { e ->
                _weatherAlertsStateFlow.value = ApiState.Failure(e)
                Log.i(TAG, "getWeatherAlerts: ${e.message}")
            }.collect {
                _weatherAlertsStateFlow.value = ApiState.Success(it)
            }
        }
    }

}