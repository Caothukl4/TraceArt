package com.tuananh.traceart.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily

object AppTypography {

    val titleMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val titleSupperLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            lineHeight = 28.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val titleLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Default,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val titleSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Default,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val bodyMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Default,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val bodySmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Default,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

    val labelMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Default,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    val labelLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Default,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
}