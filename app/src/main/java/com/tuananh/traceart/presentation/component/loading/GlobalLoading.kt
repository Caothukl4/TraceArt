package com.tuananh.traceart.presentation.component.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel

const val TAG = "GlobalLoading"

@Composable
fun GlobalLoading() {
    val viewModel2: GlobalLoadingViewModel =
        hiltViewModel()
    val isLoading by viewModel2.isLoading.collectAsState()
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(9999f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }
    }
}