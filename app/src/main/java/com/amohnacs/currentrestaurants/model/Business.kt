package com.amohnacs.currentrestaurants.model

data class Business(
    val id: String,
    val name: String,
    val displayPhone: String? = null,
    val reviewCount: Int? = null,
    val rating: Double,
    val location: YelpLocation? = null,
    val coordinates: YelpCoordinates,
    val photos: List<String?>,
    val price: String,
    val category: YelpCategory
)

data class YelpLocation(
    val address: String
)

data class YelpCoordinates(
    val latitude: Double,
    val longitude: Double
)

data class YelpCategory(
    val title: String
)