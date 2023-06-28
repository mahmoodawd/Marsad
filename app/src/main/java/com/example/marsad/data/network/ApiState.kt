package com.example.marsad.data.network

sealed class ApiState {
    class Success(val weatherStatus: OneCallResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}