package com.example.marsad.data.repositories


import com.example.marsad.data.network.OneCallResponse
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryInterface {
    suspend fun getWeatherStatus(lat:Double, lon:Double): Flow<OneCallResponse>
}