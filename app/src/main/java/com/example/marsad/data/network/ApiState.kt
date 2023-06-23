package com.example.marsad.data.network

sealed class ApiState {
    class Success(val weatherStatus: WeatherResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}