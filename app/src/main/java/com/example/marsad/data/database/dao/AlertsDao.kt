package com.example.marsad.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.marsad.data.database.entities.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("SELECT * FROM weather_alerts")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM weather_alerts where alert_id = :id")
    fun getAlertById(id: Int): Flow<AlertEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alertEntity: AlertEntity): Long

    @Delete
    suspend fun delete(alertEntity: AlertEntity): Int

    @Update
    suspend fun update(alert: AlertEntity)
}