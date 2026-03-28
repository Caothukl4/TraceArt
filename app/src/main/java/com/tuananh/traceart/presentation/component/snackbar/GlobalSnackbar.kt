package com.tuananh.traceart.presentation.component.snackbar

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuananh.traceart.presentation.shared.SnackbarType
import com.tuananh.traceart.presentation.theme.Colors

@Composable
fun GlobalSnackbar() {
    val viewModel: GlobalSnackbarViewModel =
        hiltViewModel(LocalActivity.current as ComponentActivity)
    val snackbarHostState = viewModel.snackbarHostState
    val snackbarType by viewModel.snackbarType.collectAsState()
    Box(modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues())) {
        SnackbarHost(hostState = snackbarHostState, snackbar = { snackbarData ->
            Snackbar(
                containerColor = if (snackbarType == SnackbarType.Warning) {
                    Colors.secondary
                } else {
                    Colors.primary
                }, // Màu nền
                contentColor = if (snackbarType == SnackbarType.Warning) {
                    Colors.onSecondary
                } else {
                    Colors.onPrimary
                },         // Màu chữ
                snackbarData = snackbarData
            )
        })
    }
}