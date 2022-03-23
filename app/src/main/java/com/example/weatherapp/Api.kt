package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String = "55411",
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f"
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String = "55411",
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f",
        @Query("cnt") cnt: Int = 16
    ): Forecast
}