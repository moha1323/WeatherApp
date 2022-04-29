package com.example.weatherapp.ui.search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.other.Constants.ELAPSED_TIME
import com.example.weatherapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.weatherapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.weatherapp.other.Constants.REQUEST_CODE_COARSE_LOCATION
import com.example.weatherapp.other.Constants.manifestLocationPermission
import com.example.weatherapp.service.NotificationService
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var latitude: Float? = null
    private var longitude: Float? = null
    private var notificationIsActive: Boolean = false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(30)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach{
                    Log.d(TAG,"YM1997-1/Callback Latitude: " + it.latitude.toString() + ", Longitude: " + it.longitude.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = FragmentSearchBinding.bind(view)
        viewModel = SearchViewModel()
        createNotificationChannel()
        serviceIntent = Intent(requireActivity().applicationContext, NotificationService::class.java)
    }

    override fun onResume() {
        super.onResume()
        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let {viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        viewModel.enableButton.observe(this) { enable ->
            binding.searchButton.isEnabled = enable
        }

        binding.searchButton.setOnClickListener{
            val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(viewModel.getZipCode())
            findNavController().navigate(action)
        }

        binding.locationButton.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(requireContext(), manifestLocationPermission) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            } else {
                requestLocation()
            }
        }

        binding.notificationButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), manifestLocationPermission) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            } else {
                requestLocation()
            }
        }
    }

    private fun requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), manifestLocationPermission)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("We need permission for your location to provide accurate weather data")
                .setPositiveButton("Ok"){ dialog: DialogInterface, which: Int ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(manifestLocationPermission), REQUEST_CODE_COARSE_LOCATION)
                }
                .setNegativeButton("Cancel"){ dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(manifestLocationPermission), REQUEST_CODE_COARSE_LOCATION)
        }
        runBlocking {
            delay(3000)
            requestLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.d(TAG,"YM1997-1.0 Latitude: " + location.latitude.toString() + ", Longitude: " + location.longitude.toString())
            latitude = location.latitude.toFloat()
            longitude = location.longitude.toFloat()
            Log.d(TAG,"YM1997-2.0 Latitude: " + latitude + ", Longitude: " + longitude)
            notificationCondition()
            if(latitude != null && longitude != null) {
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment("", latitude!!, longitude!!)
                findNavController().navigate(action)
            }
        }.addOnFailureListener {
            Log.d(TAG, "Failed getting current location")
        }
    }

    private fun notificationCondition(){
        if (!notificationIsActive){
            notificationIsActive = true
            serviceIntent.putExtra(ELAPSED_TIME, 0)
            requireActivity().startService(serviceIntent)
            binding.notificationButton.text = getString(R.string.turn_notification_off)
        } else {
            notificationIsActive = false
            requireActivity().stopService(serviceIntent)
            with(NotificationManagerCompat.from(requireContext())) {
                cancel(1)
            }
            binding.notificationButton.text = getString(R.string.turn_notification_on)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                description = "Weather Channel"
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

