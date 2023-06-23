package com.example.marsad.data.network

import retrofit2.Response

const val API_KEY = "1e248f8902cd91a54d8c7d45d360729e"

object LocationRemoteDataSource : RemoteSource {

    val retrofitService: ApiService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    override suspend fun getWeatherStatus(lat: String, lon: String) =
        retrofitService.getWeatherDetails(lat, lon, API_KEY, "metric")

}