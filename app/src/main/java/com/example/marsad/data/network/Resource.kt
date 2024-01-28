package com.example.marsad.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>

    class Failure(val error: Throwable? = null) : Resource<Nothing>

    data object Loading : Resource<Nothing>
}

fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this
        .map<T, Resource<T>> {
            it?.let { Resource.Success(it) } ?: Resource.Loading
        }
        .onStart {
            Resource.Loading
        }.catch {
            Resource.Failure(it)
        }
}