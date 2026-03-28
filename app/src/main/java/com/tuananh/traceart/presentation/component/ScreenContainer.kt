package com.tuananh.traceart.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tuananh.traceart.R
import com.tuananh.traceart.presentation.navigation.LocalNavController
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.presentation.theme.Colors
import com.tuananh.traceart.utils.customSystemBarsPadding

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color? = Colors.background,
    title: String? = "",
    disableBackButton: Boolean = false,
    hideHeader: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    listIconRight: List<Pair<Int, () -> Unit>> = emptyList(),
    safeBottom: Boolean = false,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val navController = LocalNavController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor!!)
            .customSystemBarsPadding(bottom = safeBottom) // Safe area
    ) {
        if (!hideHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 4.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!disableBackButton) {
                        IconButton(onClick = {
                            if (onBackClick != null) {
                                onBackClick()
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Back",
                                modifier = Modifier.size(24.dp),
                                tint = Colors.primary
                            )
                        }
                    }
                    Text(
                        text = title ?: "",
                        style = AppTypography.titleMedium
                    )
                }

                Row {
                    listIconRight.forEach { (drawableResId, onClick) ->
                        IconButton(onClick = onClick) {
                            Icon(
                                painter = painterResource(id = drawableResId),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Colors.primary
                            )
                        }
                    }
                }
            }
        }

        if (content != null) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewScreenContainer() {
    val context = LocalContext.current
    val fakeNavController = remember { NavHostController(context) }

    CompositionLocalProvider(LocalNavController provides fakeNavController) {
        ScreenContainer(title = "test")
    }
}