package com.example.marsad.ui.weatheralerts.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.repositories.AlertRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherAlertsViewModel(val repository: AlertRepositoryInterface) : ViewModel() {
    private val TAG = WeatherAlertsViewModel::class.java.simpleName

    fun getActiveAlerts(): MutableLiveData<List<AlertItem>> {
        val alertList = MutableLiveData<List<AlertItem>>()
        viewModelScope.launch {
            repository.getActiveAlerts().catch { e ->
                Log.i(TAG, "getActiveAlerts: ${e.message}")
            }.collect {
                alertList.postValue(it)
            }
        }
        return alertList
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

}