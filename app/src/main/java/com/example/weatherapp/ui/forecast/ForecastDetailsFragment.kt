package com.example.weatherapp.ui.forecast

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastDetailsFragment : Fragment() {
    private val args by navArgs<ForecastDetailsFragmentArgs>()

    @SuppressLint("UnrememberedMutableState")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                WeatherCard(args = args)
            }
        }
    }

}