package com.tuananh.traceart.presentation.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.presentation.theme.Colors

@Composable
fun Input(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (text: String) -> Unit,
    label: String? = null,
    placeholder: String = ""
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "$label:",
                style = AppTypography.titleSmall
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.primaryContainer, RoundedCornerShape(8.dp))
                .height(40.dp)
                .padding(horizontal = 16.dp),
            textStyle = AppTypography.bodyMedium,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(placeholder, style = AppTypography.labelMedium)
                    }
                    innerTextField()
                }
            },

        )
    }

}