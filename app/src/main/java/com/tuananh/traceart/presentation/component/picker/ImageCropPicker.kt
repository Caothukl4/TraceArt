package com.tuananh.traceart.presentation.component.picker

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.tuananh.traceart.R
import com.tuananh.traceart.presentation.component.button.Button
import com.tuananh.traceart.presentation.component.button.ButtonType
import com.tuananh.traceart.presentation.component.modal.CenterModal
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.utils.resizeImage
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

class ImageCropPickerController internal constructor(
    internal var openPicker: () -> Unit
)

@Composable
fun rememberImageCropPickerController(): ImageCropPickerController {
    return remember { ImageCropPickerController({}) }
}

@Composable
fun ImageCropPicker(
    controller: ImageCropPickerController = rememberImageCropPickerController(),
    onResult: (uris: List<Uri>) -> Unit,
    maxFiles: Int = 1,
    enableCrop: Boolean = true,
    maxWidth: Int? = null,
    maxHeight: Int? = null,
    showPicker: Boolean = false,
    onDismiss: () -> Unit = {},
    title: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Crop launcher
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = UCrop.getOutput(result.data!!)
            if (uri != null) {
                scope.launch {
                    val finalUri = if (maxWidth != null || maxHeight != null) {
                        resizeImage(context, uri, maxWidth, maxHeight)
                    } else uri
                    onResult(listOf(finalUri))
                }
            }
        }
    }

    val allowMultiple = maxFiles > 1

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = if (allowMultiple)
            ActivityResultContracts.GetMultipleContents()
        else ActivityResultContracts.GetContent()
    ) { result ->
        if (result == null) return@rememberLauncherForActivityResult

        val uris = when {
            allowMultiple -> (result as List<Uri>).take(maxFiles)
            else -> listOf(result as Uri)
        }

        if (enableCrop && !allowMultiple) {
            val destination = Uri.fromFile(
                File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
            )
            UCrop.of(uris.first(), destination)
                .apply {
                    if (maxWidth != null && maxHeight != null) {
                        withMaxResultSize(maxWidth, maxHeight)
                    }
                }
                .getIntent(context).also { cropLauncher.launch(it) }
        } else {
            scope.launch {
                val processedUris = uris.map { uri ->
                    if (maxWidth != null || maxHeight != null) {
                        resizeImage(context, uri, maxWidth, maxHeight)
                    } else uri
                }
                onResult(processedUris)
            }
        }
    }

    // Gán openPicker để gọi từ ngoài
    LaunchedEffect (Unit) {
        controller.openPicker = {
            galleryLauncher.launch("image/*")
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            val uri = tempCameraUri!!
            if (enableCrop) {
                val destination = Uri.fromFile(
                    File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                )
                UCrop.of(uri, destination)
                    .apply {
                        if (maxWidth != null && maxHeight != null) {
                            withMaxResultSize(maxWidth, maxHeight)
                        }
                    }
                    .getIntent(context).also { cropLauncher.launch(it) }
            } else {
                scope.launch {
                    val finalUri = if (maxWidth != null || maxHeight != null) {
                        resizeImage(context, uri, maxWidth, maxHeight)
                    } else uri
                    onResult(listOf(finalUri))
                }
            }
        }
    }

    CenterModal(showModal = showPicker, onDismiss = { onDismiss() }) {
        Column (modifier = Modifier.fillMaxWidth().padding(16.dp)){
            if (title != null) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = title,
                    style = AppTypography.titleMedium
                )
            }
            Button(
                onClick = {
                    onDismiss()
                    galleryLauncher.launch("image/*")
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.Pick_image_from_gallery),
                icon = R.drawable.ic_gallery
            )
            Button(
                onClick = {
                    onDismiss()
                    val photoFile =
                        File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
                    tempCameraUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        photoFile
                    )
                    tempCameraUri?.let { cameraLauncher.launch(it) }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.Take_photo),
                icon = R.drawable.ic_camera,
                type = ButtonType.Outline
            )
        }
    }
}