package com.aktech.ardrawing.presentation.screen.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.aktech.ardrawing.presentation.component.UpdateSystemBars
import com.aktech.ardrawing.presentation.navigation.LocalNavController
import com.aktech.ardrawing.presentation.theme.Colors

const val TAG = "[HomeScreen]"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    UpdateSystemBars(useLightBars = true)
    val navController = LocalNavController.current
    val activity = LocalActivity.current
    val homeViewModel: HomeViewModel = hiltViewModel()


    Column(
        modifier = Modifier
            .background(Colors.background)
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
    ) {
    }
}


