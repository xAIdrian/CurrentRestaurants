package com.amohnacs.currentrestaurants.common

import com.amohnacs.currentrestaurants.model.*

object MappingHelper {
    fun mapBusinessResultToBusiness(businessResult: BusinessResult?) =
        Business(
            id = businessResult?.id ?: businessResult?.placeId ?: "",
            name = businessResult?.name ?: "",
            coordinates = YelpCoordinates(
                businessResult?.geometry?.location?.lat ?: 0.0,
                businessResult?.geometry?.location?.lng ?: 0.0
            ),
            category = YelpCategory(
                businessResult?.types?.get(0) ?: "No Category"
            ),
            displayPhone = businessResult?.phoneNumber,
            location = YelpLocation(
                businessResult?.address ?: ""
            ),
            rating = businessResult?.rating
        )
}