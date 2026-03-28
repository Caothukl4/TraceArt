package com.aktech.ardrawing.presentation.component.snackbar

import androidx.lifecycle.ViewModel
import com.aktech.ardrawing.presentation.shared.GlobalSharedState
import com.aktech.ardrawing.presentation.shared.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalSnackbarViewModel @Inject constructor(private val globalSharedState: GlobalSharedState) :
    ViewModel() {
    val snackbarHostState = globalSharedState.snackbarHostState
    val snackbarType = globalSharedState.snackbarType
    fun showSnackbar(type: SnackbarType, message: String) {
        globalSharedState.showSnackbar(type, message)
    }
}