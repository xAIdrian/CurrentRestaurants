package com.amohnacs.currentrestaurants.model

data class SearchResults(
    val total: Int,
    val businesses: List<Business>
)