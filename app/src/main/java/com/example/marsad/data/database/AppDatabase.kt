package com.example.marsad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.marsad.data.database.dao.AlertsDao
import com.example.marsad.data.database.dao.LocationDoa
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.data.model.SavedLocation

@Database([SavedLocation::class, AlertItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDoa
    abstract fun getAlertsDao(): AlertsDao

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