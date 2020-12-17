package com.amohnacs.currentrestaurants.main.places

import com.amohnacs.currentrestaurants.model.BusinessResult
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.domain.YelpRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlacesViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val mainViewModel: MainViewModel,
    private val yelpRepository: YelpRepository
) : ViewModel() {

    val navigateEvent = MutableLiveData<@androidx.annotation.NavigationRes Int>()
    val errorEvent = MutableLiveData<String>()
    val emptyEvent = MutableLiveData<String>()

    val placesBurritoLiveData = MutableLiveData<List<Business>>()
    var isShowingYelpQlDataSource = MutableLiveData<Boolean>(mainViewModel.isShowingYelpQlDataSource)

    @SuppressLint("CheckResult") //for lint error of not using result of doOnError this is delegated
    fun getBurritoPlacesFromYelp(): Observable<PagingData<Business>> =
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

    @SuppressLint("CheckResult")
    fun getBurritoPlacesFromGoogle() {
        locationManager.getUsersLastLocation()
            .flatMap {
                yelpRepository.findUserPlace(
                    it.latitude,
                    it.longitude
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                placesBurritoLiveData.value = it
            }, {
                errorEvent.value = it.message
            })
    }

    fun loadNewDataSource() {
        // update datasource in main for persistence
        val updatedDataSource = !mainViewModel.isShowingYelpQlDataSource
        mainViewModel.isShowingYelpQlDataSource = updatedDataSource
        isShowingYelpQlDataSource.value = updatedDataSource

        if (isShowingYelpQlDataSource.value == true) {
            getBurritoPlacesFromYelp()
        } else {
            getBurritoPlacesFromGoogle()
        }
    }
}