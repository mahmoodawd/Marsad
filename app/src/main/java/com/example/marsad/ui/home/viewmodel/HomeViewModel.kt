package com.example.marsad.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.repositories.LocationRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val locationRepository: LocationRepositoryInterface):ViewModel() {
    val TAG = HomeViewModel::class.java.simpleName

    private val _weatherDataStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weatherDataStateFlow: StateFlow<ApiState> = _weatherDataStateFlow

    fun getWeatherStatus(lat:Double, lon:Double){
        viewModelScope.launch {
            locationRepository.getWeatherDetails(lat, lon).catch { e ->
                _weatherDataStateFlow.value = ApiState.Failure(e)
                Log.i(TAG, "getWeatherStatus: Failed: ${e.message}")
            }.collect{ weatherData->
                _weatherDataStateFlow.value = ApiState.Success(weatherData)
            }
        }
    }

}