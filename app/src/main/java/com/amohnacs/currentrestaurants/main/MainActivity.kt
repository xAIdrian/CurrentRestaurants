package com.amohnacs.currentrestaurants.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amohnacs.currentrestaurants.CurrentApp
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding
    lateinit var mainComponent: MainComponent

//    val viewModel: MainViewModel by viewModels(factory)

    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerGraph()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermissions()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getUserLocation()
            } else {
                Snackbar.make(binding.root, getString(R.string.error_permission), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initDaggerGraph() {
        mainComponent = (applicationContext as CurrentApp).appComponent.mainComponent().create()
        mainComponent.inject(this)
    }

    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSIONS_CODE
            )
        } else {
            viewModel.getUserLocation()
        }
    }

    companion object {
        private const val LOCATION_PERMISSIONS_CODE = 200
    }
}