package com.example.marsad.ui.favorites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marsad.data.model.SavedLocation

class SharedViewModel : ViewModel() {
    var savedLocationLiveData = MutableLiveData<SavedLocation>()
    fun setLocation(savedLocation: SavedLocation) {
        savedLocationLiveData.value = savedLocation
    }

}