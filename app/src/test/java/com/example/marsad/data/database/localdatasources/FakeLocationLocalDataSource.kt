package com.example.marsad.data.database.localdatasources

import com.example.marsad.data.model.SavedLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocationLocalDataSource : LocalSource<SavedLocation> {
    companion object{

        val savedLocations = mutableListOf(
            SavedLocation("Cairo", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("London", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Fayoum", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Paris", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Moscow", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Vienna", 30.0, 60.0, 36, "google.com", "rainy"),
            SavedLocation("Tokyo", 30.0, 60.0, 36, "google.com", "rainy"),
        )
    }

    override fun getAllItems(): Flow<List<SavedLocation>> = flow {
        emit(savedLocations)
    }

    override suspend fun addNewItem(item: SavedLocation): Long {
        savedLocations.add(item)
        return savedLocations.size.toLong()
    }

    override suspend fun deleteItem(item: SavedLocation): Int {
        savedLocations.remove(item)
        return savedLocations.size
    }

    override fun getItemById(itemId: Long): Flow<SavedLocation> {
        TODO("Not yet implemented")
    }
}
