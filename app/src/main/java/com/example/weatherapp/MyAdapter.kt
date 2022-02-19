package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateView: TextView = view.findViewById(R.id.date)
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)
        private val temp: TextView = view.findViewById(R.id.temp)
        private val tempHigh: TextView = view.findViewById(R.id.tempHigh)
        private val tempLow: TextView = view.findViewById(R.id.tempLow)
        private val iconView: ImageView = view.findViewById(R.id.forecast_icon)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormatter = DateTimeFormatter.ofPattern("h:mma")
        fun bind(data: DayForecast) {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val sunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val sunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            dateView.text = dateFormatter.format(dateTime)
            sunriseView.append(timeFormatter.format(sunrise))
            sunsetView.append(timeFormatter.format(sunset))
            temp.append(data.temp.day.toInt().toString() + "°")
            tempHigh.append(data.temp.max.toInt().toString() + "°")
            tempLow.append(data.temp.min.toInt().toString() + "°")
            val weather = data.weather.firstOrNull()?.icon
            Glide.with(iconView)
                .load("https://openweathermap.org/img/wn/${weather}@2x.png")
                .into(iconView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}
