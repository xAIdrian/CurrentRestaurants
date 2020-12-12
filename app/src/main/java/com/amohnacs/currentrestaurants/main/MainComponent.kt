package com.amohnacs.currentrestaurants.main

import com.amohnacs.currentrestaurants.dagger.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainActivity: MainActivity)
}