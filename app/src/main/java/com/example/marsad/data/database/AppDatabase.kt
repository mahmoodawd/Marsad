package com.example.marsad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marsad.data.database.dao.AlertsDao
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.database.dao.WeatherDetailsDoa
import com.example.marsad.data.model.*
import com.example.marsad.data.network.*

@Database(
    [SavedLocation::class, AlertItem::class, WeatherDetailsResponse::class, CurrentWeather::class,
        HourlyWeather::class,
        DailyWeather::class,
        Weather::class,
        Temperature::class,
        FeelsLikeTemperature::class], version = 1
)
@TypeConverters(
    value = [
        HourlyWeatherListConverter::class,
        DailyWeatherListConverter::class,
        WeatherListConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDoa
    abstract fun getAlertsDao(): AlertsDao
    abstract fun getWeatherDetailsDao(): WeatherDetailsDoa

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorites-db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}