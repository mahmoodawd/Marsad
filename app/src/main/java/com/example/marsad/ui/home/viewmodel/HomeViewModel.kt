package com.example.marsad.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.repositories.LocationRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val locationRepository: LocationRepositoryInterface):ViewModel() {
    private val _postStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val postStateFlow: StateFlow<ApiState> = _postStateFlow

    fun getWeatherStatus(lat:String, lon:String){
        viewModelScope.launch {
            locationRepository.getWeatherStatus(lat, lon).catch {e ->
                _postStateFlow.value = ApiState.Failure(e)
            }.collect{ weatherData->
                _postStateFlow.value = ApiState.Success(weatherData)
            }
        }
    }

}