package com.amohnacs.currentrestaurants.main

import com.amohnacs.currentrestaurants.dagger.scopes.ActivityScope
import com.amohnacs.currentrestaurants.main.map.MapsFragment
import com.amohnacs.currentrestaurants.main.places.PlacesFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainActivity: MainActivity)
    fun inject(placesFragment: PlacesFragment)
    fun inject(mapsFragment: MapsFragment)
}