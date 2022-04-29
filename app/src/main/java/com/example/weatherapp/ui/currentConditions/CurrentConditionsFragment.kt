package com.example.weatherapp.ui.currentConditions

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.ui.dialog.ErrorDialogFragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentConditionsBinding
import com.example.weatherapp.model.CurrentConditions
import com.example.weatherapp.service.WeatherService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.fragment_current_conditions) {
    private val args by navArgs<CurrentConditionsFragmentArgs>()
    private lateinit var binding: FragmentCurrentConditionsBinding
    @Inject lateinit var viewModel: CurrentConditionsViewModel
    private lateinit var weatherService: WeatherService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentConditionsBinding.bind(view)
        weatherService = WeatherService()
        binding.forecastButton.setOnClickListener{
            val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(args.zipCode, args.latitude, args.longitude)
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        Log.d(TAG, "YM1997-4 Latitude: " + args.latitude + ", Longitude: " + args.longitude)
        try {
            if(args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
                viewModel.loadData(args.zipCode)
            } else {
                viewModel.loadData(args.latitude, args.longitude)
            }
        } catch (exception: HttpException){
            if (exception.code().equals(404)){
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }
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
        weatherService.setData("Temperature: " + binding.temperature.text)
        Log.d(TAG, "Temperature: " + binding.temperature.text)
    }

}