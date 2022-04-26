package com.example.weatherapp.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.service.Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val ourForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = ourForecast

    fun loadData(zipCode: String) = runBlocking {
        launch {
            ourForecast.value = service.getForecast(zipCode)
        }
    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch {
            ourForecast.value = service.getForecastLatLon(latitude, longitude)
        }
    }
}