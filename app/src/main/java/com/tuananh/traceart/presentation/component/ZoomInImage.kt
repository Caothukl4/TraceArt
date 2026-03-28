package com.tuananh.traceart.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.io.File


@Composable
fun ZoomInImage(
    modifier: Modifier = Modifier,
    imagePath: String,
) {
    var currentImagePath by remember { mutableStateOf(imagePath) }
//    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imagePath) {
        currentImagePath = imagePath
//        isLoading = true // đặt lại loading khi imagePath thay đổi
    }

    Box(modifier = modifier) {
        AnimatedContent(
            targetState = currentImagePath,
            transitionSpec = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = 400)
                ) togetherWith  fadeOut(animationSpec = tween(100))
            },
            label = "ZoomInImageTransition"
        ) { path ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(File(path))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        // Loading indicator ở giữa


    }
}

@Preview
@Composable
fun ZoonInImagePreview (){
    ZoomInImage(imagePath = "")
}