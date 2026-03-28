package com.aktech.ardrawing.presentation.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.aktech.ardrawing.presentation.theme.AppTypography
import com.aktech.ardrawing.presentation.theme.Colors

@Composable
fun GlobalInterLoading() {
    val viewModel2: GlobalLoadingViewModel =
        hiltViewModel()
    val isInterLoading by viewModel2.isInterLoading.collectAsState()
    if (isInterLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.surface)
                .zIndex(9999f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            Text(text = "Loading Ads", style = AppTypography.labelMedium, modifier = Modifier.padding(top = 60.dp))
        }
    }
}