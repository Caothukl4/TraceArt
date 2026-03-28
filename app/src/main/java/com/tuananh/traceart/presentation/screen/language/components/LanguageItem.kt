package com.tuananh.traceart.presentation.screen.language.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tuananh.traceart.R
import com.tuananh.traceart.domain.model.Language
import com.tuananh.traceart.presentation.theme.AppTypography
import com.tuananh.traceart.presentation.theme.Colors
import com.tuananh.traceart.presentation.theme.Scan2HearTheme

@Composable
fun LanguageItem(modifier: Modifier = Modifier, language: Language, isSelected: Boolean = false, onSelectedLanguage: ((language: Language)->Unit)? = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onSelectedLanguage?.invoke(language)
            }
            .background(
                color = Colors.surface,
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(language.imgResource),
            contentDescription = null,
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
                .clip(CircleShape)
                .background(Colors.surfaceVariant)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .weight(1f)
        ) {
            Text(text = language.name, style = AppTypography.titleSmall)
        }
        RadioButton(
            modifier = Modifier.padding(end = 8.dp).size(10.dp),
            selected = isSelected,
            onClick = {onSelectedLanguage?.invoke(language)},
        )
    }
}

@Preview
@Composable
fun LanguageItemPreview(){
    Scan2HearTheme {
        LanguageItem(language = Language(imgResource = R.drawable.img_flag_de, name = "Français"))
    }
}