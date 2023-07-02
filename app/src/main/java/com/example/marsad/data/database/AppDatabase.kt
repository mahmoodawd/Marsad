package com.example.marsad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.marsad.data.model.SavedLocation

@Database([SavedLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDoa

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