package com.example.marsad.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Uses initializer to create instances from [ViewModel]s
 */
fun <VM : ViewModel> viewModelFactory(initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer().takeIf { modelClass.isAssignableFrom(it.javaClass) } as T
        }
    }
}