package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.domain.YelpRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.YelpCategory
import com.amohnacs.currentrestaurants.model.YelpCoordinates
import com.amohnacs.currentrestaurants.model.YelpLocation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlacesViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val mainViewModel: MainViewModel,
    private val yelpRepository: YelpRepository
) : ViewModel() {

    val businesses = MutableLiveData<List<Business>>()
    val navigateEvent = MutableLiveData<@androidx.annotation.NavigationRes Int>()
    val errorEvent = MutableLiveData<String>()
    val emptyEvent = MutableLiveData<String>()

    @SuppressLint("CheckResult") //for lint error of not using result of doOnError this is delegated
    fun getBurritoPlaces(): Observable<PagingData<Business>> =
        locationManager.getUsersLastLocation()
            .flatMapObservable {
                yelpRepository.getBurritoSearch(
                    it.latitude,
                    it.longitude
                )
            }.doOnError{
                errorEvent.value = it.message
            }

    fun businessSelected(clickedBusiness: Business) {
        mainViewModel.selectedBusinessId = clickedBusiness.id
        navigateEvent.value = R.id.mapsFragment
    }
}