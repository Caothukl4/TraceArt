package com.tuananh.traceart.presentation.component.loading

import androidx.lifecycle.ViewModel
import com.tuananh.traceart.presentation.shared.GlobalSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalLoadingViewModel @Inject constructor(state: GlobalSharedState) : ViewModel() {

    val isLoading = state.isLoading
    val isInterLoading = state.isInterLoading
}