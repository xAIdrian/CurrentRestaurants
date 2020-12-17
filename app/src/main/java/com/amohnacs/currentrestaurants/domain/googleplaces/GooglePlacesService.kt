package com.amohnacs.currentrestaurants.domain.googleplaces

import com.amohnacs.currentrestaurants.model.PlacesSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesService {

    @GET("maps/api/place/textsearch/json")
    fun getPlacesSearchResults(
        @Query("query") term: String?,
        @Query("location") formattedLocation: String, //42.3675294,-71.186966
        @Query("radius") inputType: String,
        @Query("key") apiKey: String
    ): Observable<PlacesSearchResponse>

    @GET("maps/api/place/details/json")
    fun getPlacesBusinessDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): Observable<PlacesSearchResponse>
}