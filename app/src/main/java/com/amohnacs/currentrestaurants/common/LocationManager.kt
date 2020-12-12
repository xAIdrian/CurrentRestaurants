package com.amohnacs.currentrestaurants.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import javax.inject.Inject

class LocationManager @Inject constructor(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var lastFetchedLocation: Location? = null

    private fun isLocationEnabled(): Boolean = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * We are checking for location availability and then launching our own asynchronous operation
     * to get the actual location once we are sure we have permissions
     */
    @SuppressLint("MissingPermission")
    fun getUsersLastLocation(getFreshLocation: Boolean = false): Single<Location> {

        return Single.create { emitter ->
            if (lastFetchedLocation != null && !getFreshLocation) {
                emitter.onSuccess(lastFetchedLocation!!)
            } else if (isLocationEnabled()) {
                fusedLocationClient
                    .locationAvailability
                    .addOnSuccessListener { locationAvailability ->
                        if (locationAvailability.isLocationAvailable) {
                            fusedLocationClient
                                .lastLocation
                                .addOnSuccessListener { location ->
                                    emitter.onSuccess(location)
                                }
                        }
                    }
            } else emitter.onError(Throwable("You haven't given the permissions"))}
    }
}