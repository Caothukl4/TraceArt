package com.aktech.ardrawing.presentation.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aktech.ardrawing.presentation.theme.Colors

@Composable
fun CenterModal(
    showModal: Boolean,
    heightFraction: Float = 1f,
    onDismiss: (() -> Unit)? = {},
    showCloseButton: Boolean = true,
    content: @Composable (() -> Unit),
) {

    if (showModal) {
        Dialog(
            onDismissRequest = { onDismiss?.invoke() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss?.invoke() } // tap ngoài modal dismiss
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxHeight * heightFraction)

                        .padding(horizontal = 20.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(Colors.surface)
                        .pointerInput(Unit) {
                            detectTapGestures { }
                        }

                ) {
                    content()
                    if (showCloseButton) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(y = (-8).dp, x = (8).dp),
                            onClick = { onDismiss?.invoke() }) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
                }
            }

        }
    }
}