package com.example.marsad.data.database.localdatasources

import kotlinx.coroutines.flow.Flow

open class GeneralLocalDataSource<T> : LocalSource<T> {
    override fun getAllItems(): Flow<List<T>> {
        TODO("Not yet implemented")
    }

    override fun getItemById(itemId: Long): Flow<T> {
        TODO("Not yet implemented")
    }


    override suspend fun deleteItem(item: T): Int {
        TODO("Not yet implemented")
    }


    override suspend fun addNewItem(item: T): Long {
        TODO("Not yet implemented")
    }
}