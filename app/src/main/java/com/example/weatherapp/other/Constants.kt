package com.example.weatherapp.other

import android.Manifest

object Constants {
    const val manifestLocationPermission : String = Manifest.permission.ACCESS_COARSE_LOCATION
    const val REQUEST_CODE_COARSE_LOCATION: Int = 1234

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT = "ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "Weather_Channel"
    const val NOTIFICATION_CHANNEL_NAME = "Weather"
    const val NOTIFICATION_ID = 1

    const val TIMER_UPDATED = "timerUpdated"
    const val ELAPSED_TIME = "elapsedTime"
}