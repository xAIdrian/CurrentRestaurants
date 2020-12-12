package com.amohnacs.currentrestaurants.model

data class Business(
    val id: String,
    val name: String,
    val displayPhone: String,
    val reviewCount: Int,
    val rating: Float,
    val location: YelpLocation,
    val coordinates: YelpCoordinates,
    val photos: List<String>
)

data class YelpLocation(
    val address: String
)

data class YelpCoordinates(
    val latitude: Float,
    val longitude: Float
)