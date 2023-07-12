package com.example.marsad.data.database.localdatasources

import android.content.Context
import com.example.marsad.data.database.AppDatabase
import com.example.marsad.data.database.dao.WeatherDetailsDoa
import com.example.marsad.data.network.WeatherDetailsResponse
import com.example.marsad.data.repositories.WeatherDetailsRepository
import kotlinx.coroutines.flow.Flow

class WeatherDetailsLocalDataSource(context: Context) :
    LocalSource<WeatherDetailsResponse> by GeneralLocalDataSource() {
    private val weatherDetailsDoa: WeatherDetailsDoa by lazy {
        AppDatabase.getInstance(context).getWeatherDetailsDao()
    }


    override fun getAllItems(): Flow<List<WeatherDetailsResponse>> =
        weatherDetailsDoa.getAll()

    override suspend fun addNewItem(item: WeatherDetailsResponse): Long =
        weatherDetailsDoa.updateWeatherDetails(item)


    override suspend fun deleteItem(item: WeatherDetailsResponse) =
        weatherDetailsDoa.clearWeatherDetails(item)

}