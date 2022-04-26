package com.example.weatherapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private var zipCode: String? = null
    private val ourEnableButton = MutableLiveData<Boolean>(false)

    val enableButton: LiveData<Boolean>
        get() = ourEnableButton

    fun updateZipCode(zipCode: String){
        if(zipCode != this.zipCode) {
            this.zipCode = zipCode
            ourEnableButton.value = isValidZipCode(zipCode)
        }
    }

    fun getZipCode(): String {
        return zipCode!!
    }

    private fun isValidZipCode(zipCode: String): Boolean{
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

}