package com.example.marsad.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val retrofitInstance: ApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create())
            ).baseUrl(BASE_URL)
            .build().create()
    }
}