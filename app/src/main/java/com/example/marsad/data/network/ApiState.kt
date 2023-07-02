package com.example.marsad.data.network

sealed class ApiState {
    class Success<T>(val weatherStatus: T) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}