package com.example.marsad.data.database

import androidx.room.*
import com.example.marsad.data.model.SavedLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDoa {
    @Query("SELECT * FROM savedLocation")
    fun getAll(): Flow<List<SavedLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(savedLocation: SavedLocation): Long

    @Delete
    suspend fun delete(savedLocation: SavedLocation): Int
}