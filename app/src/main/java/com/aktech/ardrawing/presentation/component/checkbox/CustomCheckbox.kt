package com.aktech.ardrawing.presentation.component.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aktech.ardrawing.presentation.theme.Colors

@Composable
fun CustomCheckbox(modifier: Modifier = Modifier, checked: Boolean = false) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (checked) Colors.primary else Color.Unspecified)
            .border(
                width = if (checked) 0.dp else 1.dp,
                shape = RoundedCornerShape(6.dp),
                color = if (checked) Color.Unspecified else Colors.onBackground
            )
    ) {
        if (checked) {
            Icon(
                modifier = Modifier.size(16.dp).align(Alignment.Center),
                imageVector = Icons.Filled.Check,
                tint = Colors.onPrimary,
                contentDescription = null
            )

        }
    }
}