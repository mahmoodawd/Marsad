package com.example.marsad.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.data.repositories.LocationRepositoryInterface
import com.example.marsad.ui.favorites.viewmodel.FavoritesViewModel

class MyViewModelFactory(private val _repo: LocationRepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(_repo) as T
        } else if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            FavoritesViewModel(_repo) as T
        } else {
            throw java.lang.IllegalArgumentException("No Such ViewModel")
        }
    }
}