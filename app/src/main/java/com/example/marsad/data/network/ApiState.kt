package com.example.marsad.data.network

sealed class ApiState {
    class Success<T>(val weatherStatus: T) : ApiState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Success<*>) return false

            if (weatherStatus != other.weatherStatus) return false

            return true
        }

        override fun hashCode(): Int {
            return weatherStatus?.hashCode() ?: 0
        }
    }

    class Failure(val msg: Throwable) : ApiState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Failure) return false

            if (msg != other.msg) return false

            return true
        }

        override fun hashCode(): Int {
            return msg.hashCode()
        }
    }

    object Loading : ApiState()
}