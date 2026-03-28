package com.aktech.ardrawing.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aktech.ardrawing.R
import com.aktech.ardrawing.presentation.theme.AppTypography
import com.aktech.ardrawing.presentation.theme.Colors
import com.aktech.ardrawing.presentation.theme.Scan2HearTheme

enum class ButtonType {
    Primary, Secondary, Outline, Linking
}

enum class ButtonSize {
    Large, Medium, Small
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Primary,
    text: String = "",
    icon: Int? = null,
    iconSize: Dp? = null,
    onClick: (() -> Unit)? = {},
    tintIcon: Color = Color.Unspecified,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
) {

    var modifierOk = modifier.alpha(if (enabled) 1f else 0.5f)
    val shape = RoundedCornerShape(8.dp)
    if (type != ButtonType.Linking) {
        modifierOk = modifierOk.clip(shape)
    }
    if (type == ButtonType.Outline) {
        modifierOk = modifierOk
            .background(Colors.primaryContainer)
            .border(width = 1.dp, color = Colors.outline, shape = shape)

    } else if (type == ButtonType.Secondary) {
        modifierOk = modifierOk.background(Colors.secondary)
    } else if (type == ButtonType.Primary) {
        modifierOk = modifierOk.background(Colors.primary)
    }

    modifierOk =
        modifierOk
            .clickable(enabled = enabled) { onClick?.invoke() }
            .padding(
                horizontal = 20.dp,
                vertical = if (size == ButtonSize.Small) 8.dp else if (size == ButtonSize.Large) 14.dp else 12.dp
            )

    Row(
        modifier = modifierOk,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(iconSize ?: 16.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = tintIcon
            )
        }
        Text(
            text = text,
            style = AppTypography.titleSmall.copy(textDecoration = if (type == ButtonType.Linking) TextDecoration.Underline else TextDecoration.None),
            color = if (type == ButtonType.Linking) Colors.onSurfaceVariant else if (type == ButtonType.Outline) Colors.onSurface else Colors.onPrimary
        )
    }
}

@Preview
@Composable
fun ButtonPreview() {
    Scan2HearTheme {
        Button(text = "hello", icon = R.drawable.ic_scan, type = ButtonType.Outline)
    }
}