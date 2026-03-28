package com.tuananh.traceart.presentation.navigation

import androidx.lifecycle.ViewModel
import com.tuananh.traceart.domain.repository.CommonFirebaseRepository
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