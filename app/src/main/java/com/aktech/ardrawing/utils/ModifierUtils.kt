package com.aktech.ardrawing.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.customBorder(
    strokeWidth: Dp,
    color: Color,
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false
): Modifier = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        if (top) {
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(width, 0f),
                strokeWidth = strokeWidthPx
            )
        }
        if (bottom) {
            drawLine(
                color = color,
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = strokeWidthPx
            )
        }
        if (start) {
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(0f, height),
                strokeWidth = strokeWidthPx
            )
        }
        if (end) {
            drawLine(
                color = color,
                start = Offset(width, 0f),
                end = Offset(width, height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
fun Modifier.customSystemBarsPadding(
    top: Boolean = true,
    bottom: Boolean = false
): Modifier {
    val insets = WindowInsets.systemBars.asPaddingValues()
    return this.then(
        Modifier.padding(
            top = if (top) insets.calculateTopPadding() else 0.dp,
            bottom = if (bottom) insets.calculateBottomPadding() else 0.dp
        )
    )
}

@Composable
fun Modifier.shimmer(cornerRadius: Dp = 0.dp): Modifier {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f)
    )
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing)
        )
    )

    return this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            end = Offset(translateAnim + size.width / 1.5f, size.height)
        )
        val cornerPx = cornerRadius.toPx()
        onDrawWithContent {
            drawRoundRect(brush = brush, cornerRadius = CornerRadius(cornerPx, cornerPx), size = size)
        }
    }
}
