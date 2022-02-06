package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity() {

    val temperature = listOf<ForecastTemp>(
        ForecastTemp(27F,18F, 37F),
        ForecastTemp(35F,34F, 36F),
        ForecastTemp(21F,19F, 23F),
        ForecastTemp(33F,30F, 36F),
        ForecastTemp(17F,14F, 21F),
        ForecastTemp(11F,5F, 18F),
        ForecastTemp(22F,14F, 30F),
        ForecastTemp(33F,30F, 36F),
        ForecastTemp(29F,28F, 30F),
        ForecastTemp(18F,12F, 25F),
        ForecastTemp(20F,10F, 30F),
        ForecastTemp(19F,8F, 31F),
        ForecastTemp(19F,12F, 27F),
        ForecastTemp(20F,16F, 25F),
        ForecastTemp(17F,11F, 24F),
        ForecastTemp(13F,7F, 20F)
    )

    val adapterData = listOf<DayForecast>(
        DayForecast(1644326624, 1644326624, 1644363104, temperature.get(0), 985F, 61),
        DayForecast(1644412964, 1644412964, 1644449564, temperature.get(1), 1005F, 56),
        DayForecast(1644499304, 1644499304, 1644536084, temperature.get(2), 1009F, 48),
        DayForecast(1644585584, 1644585584, 1644622544, temperature.get(3), 995F, 65),
        DayForecast(1644671924, 1644671924, 1644709004, temperature.get(4), 985F, 57),
        DayForecast(1644758204, 1644758204, 1644795524, temperature.get(5), 992F, 68),
        DayForecast(1644844544, 1644844544, 1644881984, temperature.get(6), 992F, 58),
        DayForecast(1644930824, 1644930824, 1644968504, temperature.get(7), 978F, 61),
        DayForecast(1645017164, 1645017164, 1645054964, temperature.get(8), 985F, 60),
        DayForecast(1645103444, 1645103444, 1645141484, temperature.get(9), 971F, 83),
        DayForecast(1645189784, 1645189784, 1645227944, temperature.get(10), 982F, 63),
        DayForecast(1645276064, 1645276064, 1645314404, temperature.get(11), 1005F, 55),
        DayForecast(1645362344, 1645362344, 1645400924, temperature.get(12), 985F, 57),
        DayForecast(1645448684, 1645448684, 1645487384, temperature.get(13), 995F, 57),
        DayForecast(1645534964, 1645534964, 1645573904, temperature.get(14), 971F, 55),
        DayForecast(1645621244, 1645621244, 1645660364, temperature.get(15), 988F, 64)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar = supportActionBar
        actionBar!!.title = "Forecast Activity"

        biweekly_forecast.layoutManager = LinearLayoutManager(this)
        biweekly_forecast.adapter = MyAdapter(adapterData);
    }
}