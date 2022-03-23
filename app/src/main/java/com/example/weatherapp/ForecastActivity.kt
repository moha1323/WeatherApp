package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"

        binding.biweeklyForecast.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) { forecast ->
            bindData(forecast)
        }
        viewModel.loadData()
    }

    private fun bindData(forecast: Forecast) {
        binding.biweeklyForecast.adapter = MyAdapter(forecast.list)
    }

}