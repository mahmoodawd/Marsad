package com.example.marsad.data.database.localdatasources

import kotlinx.coroutines.flow.Flow


interface LocalSource <T>{
    fun getAllItems(): Flow<List<T>>
    suspend fun addNewItem(item: T): Long
    suspend fun deleteItem(item: T): Int

}