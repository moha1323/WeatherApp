package com.example.weatherapp.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private val manifestLocationPermission : String = Manifest.permission.ACCESS_COARSE_LOCATION
    private val REQUEST_CODE_COARSE_LOCATION: Int = 1234
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var latitude: Float? = null
    var longitude: Float? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = FragmentSearchBinding.bind(view)
        viewModel = SearchViewModel()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation = p0.lastLocation
                Log.d(TAG,"YM1997-1 Latitude: " + lastLocation.latitude.toString() + ", Longitude: " + lastLocation.longitude.toString())
                latitude = lastLocation.latitude.toFloat()
                longitude = lastLocation.longitude.toFloat()
                Log.d(TAG,"YM1997-2 Latitude: " + latitude + ", Longitude: " + longitude)
                super.onLocationResult(p0)
            }
        }
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
            if (checkForPermission()){
                Log.d(TAG,"YM1997-2.0 Latitude: " + latitude + ", Longitude: " + longitude)
                if(latitude != null && longitude != null) {
                    val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment("", latitude!!, longitude!!)
                    findNavController().navigate(action)
                }
                Toast.makeText(requireContext(),"Click until you get to Current Conditions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkForPermission() : Boolean {
        val checkSelfPermission: Boolean = ActivityCompat.checkSelfPermission(requireContext(), manifestLocationPermission) == PackageManager.PERMISSION_GRANTED
        if(checkSelfPermission){
            requestLocation()
        } else {
            requestPermission()
        }
        return checkSelfPermission
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
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation(){
        val locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

}
