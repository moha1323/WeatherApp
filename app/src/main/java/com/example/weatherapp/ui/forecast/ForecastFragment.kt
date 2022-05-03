package com.example.weatherapp.ui.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.model.Forecast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    private val args by navArgs<ForecastFragmentArgs>()
    private lateinit var binding: FragmentForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)
        binding.biweeklyForecast.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) { forecast ->
            bindData(forecast)
        }
        if(args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
            viewModel.loadData(args.zipCode)
        } else {
            viewModel.loadData(args.latitude, args.longitude)
        }
    }

    private fun bindData(forecast: Forecast) {
        var adapter = MyAdapter(forecast.list)
        binding.biweeklyForecast.adapter = adapter
        adapter.setOnDayClickListener(object : MyAdapter.OnDayListener{
            override fun onDayClick(index: Int) {
                val action = ForecastFragmentDirections.actionForecastFragmentToForecastDetailsFragment(
                    viewModel.forecast.value!!.list[index])
                findNavController().navigate(action)
            }

        })
    }

}