package com.amohnacs.currentrestaurants.main.map

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.domain.YelpRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.YelpCategory
import com.amohnacs.currentrestaurants.model.YelpCoordinates
import com.amohnacs.currentrestaurants.model.YelpLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mainViewModel: MainViewModel,
    private val yelpRepository: YelpRepository
): ViewModel() {

    val business = MutableLiveData<Business>()
    val errorEvent = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun getBusinessDetails() {
        val id = mainViewModel.selectedBusinessId
        if (id != null) {
            yelpRepository.getBusinessDetailsWithId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response.hasErrors()) {
                            errorEvent.value = "response has errors"
                        } else {
                            val businessResponse = response.data?.business
                            business.value = Business(
                                id = businessResponse?.id ?: "",
                                name = businessResponse?.name ?: "",
                                displayPhone = businessResponse?.display_phone,
                                reviewCount = businessResponse?.review_count,
                                price = businessResponse?.price ?: "",
                                rating = businessResponse?.rating ?: 0.0,
                                coordinates = YelpCoordinates(
                                    businessResponse?.coordinates?.latitude ?: 0.0,
                                    businessResponse?.coordinates?.longitude ?: 0.0
                                ),
                                photos = businessResponse?.photos ?: emptyList<String>(),
                                category = YelpCategory(
                                    businessResponse?.categories?.get(0)?.title ?: "No Category"
                                ),
                                location = YelpLocation(businessResponse?.location?.formatted_address.toString())
                            )
                        }
                    },
                    {
                        errorEvent.value = it.message
                    })
        } else {
            errorEvent.value = "business ID is missing for some reason...go back!"
        }
    }
}