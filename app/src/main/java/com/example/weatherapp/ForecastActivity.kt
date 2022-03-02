package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_forecast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastActivity : AppCompatActivity() {

    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/forecast/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
        biweekly_forecast.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val call: Call<Forecast> = api.getForecast()
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {
                val forecast = response.body()
                forecast?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun bindData(forecast: Forecast) {
        biweekly_forecast.adapter = MyAdapter(forecast.list)
    }

}