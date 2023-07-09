package com.example.marsad.data.database.dao

import androidx.room.*
import com.example.marsad.data.model.AlertItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("SELECT * FROM alert")
    fun getAllAlerts(): Flow<List<AlertItem>>

    @Query("SELECT * FROM alert where id = :id")
    fun getAlertById(id: Long): Flow<AlertItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alertItem: AlertItem): Long

    @Delete
    suspend fun delete(alertItem: AlertItem): Int

    @Update
    suspend fun update(alertItem: AlertItem)
}