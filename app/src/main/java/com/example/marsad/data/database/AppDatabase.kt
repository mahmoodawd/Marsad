package com.example.marsad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marsad.data.database.dao.AlertsDao
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.database.dao.WeatherDetailsDoa
import com.example.marsad.data.database.entities.AlertEntity
import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.data.database.entities.WeatherForecastEntity
import com.example.marsad.data.database.typeconverters.TimeStampConverter
import com.example.marsad.data.database.typeconverters.WeatherConverter

private const val DATA_BASE_NAME = "favorites-db"

@Database(
    [LocationEntity::class, AlertEntity::class, WeatherForecastEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        TimeStampConverter::class,
        WeatherConverter::class
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
                    DATA_BASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}