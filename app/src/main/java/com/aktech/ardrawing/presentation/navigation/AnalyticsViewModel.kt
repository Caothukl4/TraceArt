package com.aktech.ardrawing.presentation.navigation

import androidx.lifecycle.ViewModel
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(private val commonFirebaseRepository: CommonFirebaseRepository): ViewModel(){

    fun trackScreens(screenName: String){
        val screenNameOk = screenName.substringBefore("/")
        commonFirebaseRepository.trackScreens(screenNameOk)
    }

    fun logEvent(eventName: String){
        commonFirebaseRepository.logEvent(eventName)
    }
}