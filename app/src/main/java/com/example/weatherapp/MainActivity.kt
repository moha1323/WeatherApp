package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        viewModel.loadData()
    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        binding.low.text = getString(R.string.low, currentConditions.main.tempMin.toInt())
        binding.high.text = getString(R.string.high, currentConditions.main.tempMax.toInt())
        binding.humidity.text = getString(R.string.humidity, currentConditions.main.humidity.toInt())
        binding.pressure.text = getString(R.string.pressure, currentConditions.main.pressure.toInt())
        val weather = currentConditions.weather.firstOrNull()
        weather?.let {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .into(binding.weatherIcon)
        }

    }
}