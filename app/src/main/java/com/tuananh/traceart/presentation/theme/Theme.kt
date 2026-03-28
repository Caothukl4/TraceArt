package com.tuananh.traceart.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary =  Color(0xFF5878E4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xffe0e8ff),
    secondary = Color(0xFFFFAE00),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF0F1F2),
    onBackground = Color(0xFFAAAAAA),
    surface = Color(0xffffffff),
    onSurface = Color(0xFF2D2D2D),
    surfaceVariant = Color(0xffE9E9E9),
    onSurfaceVariant = Color(0xffAAAAAA),
    outline = Color(0xffAAAAAA),
    inverseSurface = Color(0xff000000),
    inverseOnSurface = Color(0xffffffff),
    surfaceContainerHigh = Color(0xffffffff),
    error = Color(0xffD0463B),
    errorContainer = Color(0xffcdb0ad),
    onError = Color(0xffffffff)
)

private val LightColorScheme = lightColorScheme(
    primary =  Color(0xFF5878E4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEAF5FF),
    secondary = Color(0xFFFFAE00),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF0F1F2),
    onBackground = Color(0xFFAAAAAA),
    surface = Color(0xffffffff),
    onSurface = Color(0xFF2D2D2D),
    surfaceVariant = Color(0xffE9E9E9),
    onSurfaceVariant = Color(0xffAAAAAA),
    outline = Color(0xffAAAAAA),
    inverseSurface = Color(0xff000000),
    inverseOnSurface = Color(0xffffffff),
    surfaceContainerHigh = Color(0xffffffff),
    error = Color(0xffD0463B),
    errorContainer = Color(0xffcdb0ad),
    onError = Color(0xffffffff)
)

@Composable
fun Scan2HearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val Colors @Composable get() = MaterialTheme.colorScheme
val Typographies @Composable get() = MaterialTheme.typography