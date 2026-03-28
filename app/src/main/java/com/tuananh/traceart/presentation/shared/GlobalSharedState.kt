package com.tuananh.traceart.presentation.shared

import androidx.compose.material3.SnackbarHostState
import com.tuananh.traceart.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class SnackbarType {
    Success,
    Warning
}

@Singleton
class GlobalSharedState @Inject constructor(@ApplicationScope private val appScope: CoroutineScope) {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    val snackbarHostState = SnackbarHostState()
    private val _snackbarType = MutableStateFlow(SnackbarType.Success)
    val snackbarType: StateFlow<SnackbarType> = _snackbarType
    private val _isInterLoading = MutableStateFlow(false)
    val isInterLoading: StateFlow<Boolean> = _isInterLoading

     fun showSnackbar(type: SnackbarType, message: String){
         appScope.launch {
             _snackbarType.value = type
             snackbarHostState.currentSnackbarData?.dismiss()
             snackbarHostState.showSnackbar( message)
         }
    }

    fun setLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }

    fun setInterLoading(isLoading: Boolean){
        _isInterLoading.value = isLoading
    }
}
