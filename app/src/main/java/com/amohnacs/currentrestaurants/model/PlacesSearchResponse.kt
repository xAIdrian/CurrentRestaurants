package com.amohnacs.currentrestaurants.model

import com.google.gson.annotations.SerializedName

data class PlacesSearchResponse (
    val results: List<BusinessResult>? = null,
    val result: BusinessResult? = null,
    val status: String? = null
)

data class BusinessResult (
    val geometry: Geometry? = null,
    val icon: String? = null,
    val id: String? = null,
    @SerializedName("place_id") val placeId: String? = null,
    val name: String? = null,
    @SerializedName("opening_hours") val openingHours: OpeningHours? = null,
    val photos: List<Photo>? = null,
    val reference: String? = null,
    val types: List<String>? = null,
    val vicinity: String? = null,
    @SerializedName("formatted_address") val address: String? = null,
    @SerializedName("formatted_phone_number") val phoneNumber: String? = null,
    val rating: Double? = 0.0,
    val url: String? = null,
    val website: String? = null
)

data class Location (
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class Geometry (
    val location: Location? = null
)

data class OpeningHours (
    @SerializedName("open_now") val openNow: Boolean = false
)

data class Photo (
    val height: Int = 0,
    @SerializedName("photo_reference") val photoReference: String? = null,
    val width: Int = 0
)