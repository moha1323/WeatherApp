package com.example.weatherapp.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.loadPicture

@Composable
fun WeatherCard(args: ForecastDetailsFragmentArgs){
    Card{
        Column(modifier = Modifier.padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Column {
                    Text(text = "" + args.dayForecast.temp.day.toInt().toString() + "°", fontSize = 72.sp)
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = "" + args.dayForecast.weather.firstOrNull()?.description)
                }
                Row(modifier = Modifier.padding(top = 20.dp)) {
                    val weather = args.dayForecast.weather.firstOrNull()?.icon
                    val url = "https://openweathermap.org/img/wn/${weather}@2x.png"
                    args.dayForecast.weather.firstOrNull()?.icon.let {
                        val image =
                            loadPicture(url = url, defaultImage = Constants.DEFAULT_IMAGE).value
                        image?.let { img ->
                            Image(
                                bitmap = img.asImageBitmap(),
                                contentDescription = "weater icon",
                                modifier = Modifier
                                    .requiredWidth(67.dp)
                                    .requiredHeight(67.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(21.dp))
            Column {
                Text(text = "Low: " + args.dayForecast.temp.min.toInt() + "°", fontSize = 17.sp)
                Text(text = "High: " + args.dayForecast.temp.max.toInt() + "°", fontSize = 17.sp)
                Text(text = "Humidity: " + args.dayForecast.humidity + "%", fontSize = 17.sp)
                Text(text = "Pressure: " + args.dayForecast.pressure.toInt().toString() + " hPa", fontSize = 17.sp)
                Text(text = "Wind Speed: " + args.dayForecast.speed.toString() + " mph", fontSize = 17.sp)
            }
        }
    }
}
