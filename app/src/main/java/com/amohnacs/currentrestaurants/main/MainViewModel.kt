package com.amohnacs.currentrestaurants.main

import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.dagger.scopes.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainViewModel @Inject constructor(
    private val locationManager: LocationManager
): ViewModel() {

    fun getUserLocation() {
        locationManager.getUsersLastLocation(true)
    }
}