package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.domain.YelpRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.YelpCategory
import com.amohnacs.currentrestaurants.model.YelpCoordinates
import com.amohnacs.currentrestaurants.model.YelpLocation
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

    @SuppressLint("CheckResult")
    fun getBurritoPlaces() {
        locationManager.getUsersLastLocation()
            .flatMapObservable {
                yelpRepository.getBurritoSearchResults(
                    it.latitude,
                    it.longitude
                )
            }.doOnError{
                errorEvent.value = it.message
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.hasErrors()) {
                        errorEvent.value = "response has errors"
                    } else {
                        val businessesResponse = response.data?.search?.business
                        if (businessesResponse?.isNotEmpty() == true) {
                            val businessList = ArrayList<Business>()
                            businessesResponse.forEach {
                                businessList.add(
                                    Business(
                                        id = it?.id ?: "",
                                        name = it?.name ?: "",
                                        price = it?.price ?: "",
                                        rating = it?.rating ?: 0.0,
                                        coordinates = YelpCoordinates(
                                            it?.coordinates?.latitude ?: 0.0,
                                            it?.coordinates?.longitude ?: 0.0
                                        ),
                                        photos = it?.photos ?: emptyList<String>(),
                                        category = YelpCategory(
                                            it?.categories?.get(0)?.title ?: "No Category"
                                        ),
                                        location = YelpLocation(it?.location?.formatted_address.toString())
                                    )
                                )
                            }
                            businesses.postValue(businessList)
                        } else {
                            emptyEvent.value = "No businesses found"
                        }
                    }
                },
                {
                    errorEvent.value = it.message
                }
            )
    }

    fun businessSelected(clickedBusiness: Business) {
        mainViewModel.selectedBusinessId = clickedBusiness.id
        navigateEvent.value = R.id.mapsFragment
    }
}