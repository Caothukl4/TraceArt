package com.aktech.ardrawing.presentation.component

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemBars(
    useLightBars: Boolean, // true = sáng, false = tối
) {
    val activity = LocalActivity.current as ComponentActivity
    val backgroundColor = if (useLightBars) Color.White else Color.Black
    val currentUseLightBars by rememberUpdatedState(useLightBars)

    LaunchedEffect (currentUseLightBars) {
        activity.enableEdgeToEdge(
            statusBarStyle = if (currentUseLightBars) {
                SystemBarStyle.light(
                    scrim = backgroundColor.toArgb(),
                    darkScrim = backgroundColor.toArgb()
                )
            } else {
                SystemBarStyle.dark(
                    scrim = backgroundColor.toArgb()
                )
            },
            navigationBarStyle = if (currentUseLightBars) {
                SystemBarStyle.light(
                    scrim = backgroundColor.toArgb(),
                    darkScrim = backgroundColor.toArgb()
                )
            } else {
                SystemBarStyle.dark(
                    scrim = backgroundColor.toArgb()
                )
            }
        )
    }

}

@Composable
fun UpdateSystemBars(
    useLightBars: Boolean
) {
    val activity = LocalActivity.current
    val view = LocalView.current
    DisposableEffect (useLightBars) {
        val window = activity?.window
        if (window != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                val decorView = window.decorView
                decorView.post {
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                    window.navigationBarColor = android.graphics.Color.TRANSPARENT
                }
            }
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = useLightBars
            controller.isAppearanceLightNavigationBars = useLightBars
        }
        onDispose {  }
    }
}