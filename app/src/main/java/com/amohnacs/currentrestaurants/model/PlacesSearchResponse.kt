package com.amohnacs.currentrestaurants.model

import com.google.gson.annotations.SerializedName

data class PlacesSearchResponse (
    var results: List<BusinessResult>? = null,
    var status: String? = null
)

data class BusinessResult (
    var geometry: Geometry? = null,
    var icon: String? = null,
    var id: String? = null,
    var name: String? = null,
    @SerializedName("opening_hours") var openingHours: OpeningHours? = null,
    var photos: List<Photo>? = null,
    @SerializedName("place_id") var placeId: String? = null,
    var reference: String? = null,
    var types: List<String>? = null,
    var vicinity: String? = null
)

data class Location (
    var lat: Double = 0.0,
    var lng: Double = 0.0
)

data class Geometry (
    var location: Location? = null
)

data class OpeningHours (
    @SerializedName("open_now") var openNow: Boolean = false
)

data class Photo (
    var height: Int = 0,
    @SerializedName("photo_reference") var photoReference: String? = null,
    var width: Int = 0
)