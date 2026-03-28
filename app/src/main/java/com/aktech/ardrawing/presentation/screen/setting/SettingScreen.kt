package com.aktech.ardrawing.presentation.screen.setting

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aktech.ardrawing.presentation.component.UpdateSystemBars
import com.aktech.ardrawing.presentation.theme.Colors
import com.aktech.ardrawing.presentation.theme.Scan2HearTheme

@Composable
fun SettingScreen() {

    SettingContainer()
}

@Composable
fun SettingContainer() {
    UpdateSystemBars(useLightBars = true)
    val context = LocalContext.current
    val activity = LocalActivity.current

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .background(Colors.background)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    Scan2HearTheme {
        SettingContainer()
    }
}