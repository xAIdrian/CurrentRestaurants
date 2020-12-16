package com.amohnacs.currentrestaurants.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amohnacs.currentrestaurants.CurrentApp
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding
    lateinit var mainComponent: MainComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerGraph()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.apply {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
            setupNavigationUI(this)
        }
        checkLocationPermissions()
    }

    override fun onSupportNavigateUp(): Boolean {
        getNavController().navigate(R.id.action_mapsFragment_to_placesFragment)
        return super.onSupportNavigateUp()
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

    private fun setupNavigationUI(toolbar: Toolbar) {
        val navController = getNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        toolbar.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun getNavController(): NavController {
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFrag.navController
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