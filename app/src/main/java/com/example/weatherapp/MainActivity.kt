package com.example.weatherapp

import android.content.Intent
import android.content.pm.LabeledIntent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.other.Constants.ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT
import com.example.weatherapp.other.Constants.REQUEST_CODE_COARSE_LOCATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnRequestPermissionsResultCallback{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val config = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolBar).setupWithNavController(navController, config)
        navigateToCurrentConditionFragmentIfNeeded(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_COARSE_LOCATION){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToCurrentConditionFragmentIfNeeded(intent)
    }

    private fun navigateToCurrentConditionFragmentIfNeeded(intent: Intent?){
        if(intent?.action == ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT){
            val navController = findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.action_global_currentConditionFragment)
        }
    }

}