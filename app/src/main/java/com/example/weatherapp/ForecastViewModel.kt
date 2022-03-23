package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val ourForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = ourForecast

    fun loadData() = runBlocking {
        launch {
            ourForecast.value = service.getForecast()
        }
    }
}