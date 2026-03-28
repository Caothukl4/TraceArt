package com.aktech.ardrawing.presentation.component.loading

import androidx.lifecycle.ViewModel
import com.aktech.ardrawing.presentation.shared.GlobalSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalLoadingViewModel @Inject constructor(state: GlobalSharedState) : ViewModel() {

    val isLoading = state.isLoading
    val isInterLoading = state.isInterLoading
}