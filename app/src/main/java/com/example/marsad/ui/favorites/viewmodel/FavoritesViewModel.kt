package com.example.marsad.ui.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.repositories.LocationRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(private val repository: LocationRepositoryInterface) : ViewModel() {
    private val TAG = FavoritesViewModel::class.java.simpleName

    private val _locationWeatherStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Loading)
    val locationWeatherStateFlow: StateFlow<ApiState> = _locationWeatherStateFlow


    fun getSavedLocations(): MutableLiveData<List<SavedLocation>> {
        val locationList = MutableLiveData<List<SavedLocation>>()
        viewModelScope.launch {
            repository.getSavedLocations().catch { e ->
                Log.i(TAG, "getSavedLocations: ${e.message}")
            }.collect {
                locationList.postValue(it)
            }
        }
        return locationList
    }

    fun addLocation(savedLocation: SavedLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLocation(savedLocation)
        }
    }

    fun removeLocation(savedLocation: SavedLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLocation(savedLocation)
        }
    }

    fun getLocationWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getLocationWeather(lat, lon).catch { e ->
                _locationWeatherStateFlow.value = ApiState.Failure(e)
            }.collect {
                _locationWeatherStateFlow.value = ApiState.Success(it)
            }
        }
    }
}