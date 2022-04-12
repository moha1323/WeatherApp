package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.CurrentConditions
import com.example.weatherapp.service.Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrentConditionsViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val ourCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = ourCurrentConditions

    fun loadData(zipCode: String) = runBlocking {
        launch {
            ourCurrentConditions.value = service.getCurrentConditions(zipCode)
        }
    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch {
            ourCurrentConditions.value = service.getCurrentConditionsLatLon(latitude, longitude)
        }
    }
}