package com.example.weatherapp.service

import android.app.*
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.weatherapp.MainActivity
import com.example.weatherapp.other.Constants.ACTION_PAUSE_SERVICE
import com.example.weatherapp.other.Constants.ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT
import com.example.weatherapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.weatherapp.other.Constants.ACTION_STOP_SERVICE
import com.example.weatherapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.weatherapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.weatherapp.other.Constants.NOTIFICATION_ID
import com.example.weatherapp.ui.currentConditions.CurrentConditionsFragment
import com.example.weatherapp.ui.search.SearchFragment

class WeatherService : LifecycleService() {

    private var isInitialRun = true
    private var data : String = "Temperature: "

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isInitialRun) {
                        Log.d(TAG, "***Started Service***")
                        startForegroundService()
                        isInitialRun = false
                    } else {
                        Log.d(TAG, "***Resumed Service***")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d(TAG, "***Paused Service***")
                }
                ACTION_STOP_SERVICE -> {
                    Log.d(TAG, "***Stopped Service***")
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Current Weather")
            .setContentText(data)
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this, 0, Intent(this, MainActivity::class.java).also{
            it.action = ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT
        }, FLAG_UPDATE_CURRENT
    )

    fun setData(weatherData : String){
        data = weatherData
    }

}