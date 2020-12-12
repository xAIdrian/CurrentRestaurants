package com.amohnacs.currentrestaurants.dagger

import com.amohnacs.currentrestaurants.main.MainComponent
import dagger.Module

@Module(subcomponents = [MainComponent::class])
class SubcomponentsModule {}