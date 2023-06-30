package com.example.marsad.data.network

import androidx.appcompat.app.AppCompatDelegate


const val API_KEY = "d74e0e363a439e102e4a72c39c09de0b"

object LocationRemoteDataSource : RemoteSource {

    val retrofitService: ApiService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    override suspend fun getWeatherStatus(lat: Double, lon: Double) =
        retrofitService.getWeatherDetails(
            lat,
            lon,
            API_KEY,
            "metric",
            "minutely"
        )


}