package com.tuananh.traceart.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp

data class WindowSize(val width: Dp, val height: Dp)

@Composable
fun getWindowSize(): WindowSize {
    val view = LocalView.current
    val density = LocalDensity.current
    val widthDp = with(density) { view.width.toDp() }
    val heightDp = with(density) { view.height.toDp() }
    return WindowSize(widthDp, heightDp)
}