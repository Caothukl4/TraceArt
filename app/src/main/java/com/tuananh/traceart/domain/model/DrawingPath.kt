package com.tuananh.traceart.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawingPath(
    val points: List<Offset>,
    val color: Color = Color.Black,
    val strokeWidth: Float = 5f,
    val isEraser: Boolean = false
)
