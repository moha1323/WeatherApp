package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val ourCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = ourCurrentConditions

    fun loadData() = runBlocking {
        launch {
            ourCurrentConditions.value = service.getCurrentConditions()
        }
    }
}