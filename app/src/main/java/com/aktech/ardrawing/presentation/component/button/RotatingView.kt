package com.aktech.ardrawing.presentation.component.button

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun RotatingView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing) // 800ms cho 1 vòng
        ),
        label = ""
    )

    val rotationZZ = if (isLoading) rotation else 0f

    Box(
        modifier = modifier
            .graphicsLayer { rotationZ = rotationZZ }
    ) {
        if (content != null) {
            content()
        }
    }
}