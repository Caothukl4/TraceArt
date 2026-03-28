package com.aktech.ardrawing.presentation.screen.language

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aktech.ardrawing.R
import com.aktech.ardrawing.data.local.provider.StaticDataProvider
import com.aktech.ardrawing.domain.model.Language
import com.aktech.ardrawing.presentation.component.UpdateSystemBars
import com.aktech.ardrawing.presentation.navigation.LocalNavController
import com.aktech.ardrawing.presentation.navigation.Routes
import com.aktech.ardrawing.presentation.screen.language.components.LanguageItem
import com.aktech.ardrawing.presentation.theme.AppTypography
import com.aktech.ardrawing.presentation.theme.Colors
import com.aktech.ardrawing.presentation.theme.Scan2HearTheme
import com.aktech.ardrawing.utils.customSystemBarsPadding
import kotlinx.coroutines.launch

@Composable
fun ChooseLanguageScreen() {
    UpdateSystemBars(useLightBars = true)
    val navController = LocalNavController.current
    val activity = LocalActivity.current
    val chooseLanguageViewModel: ChooseLanguageViewModel = hiltViewModel()
    val listLanguages by chooseLanguageViewModel.listLanguages.collectAsState()
    val saveSelectedLanguage by chooseLanguageViewModel.savedLanguage.collectAsState()
    val selectedLanguage = remember{ mutableStateOf(saveSelectedLanguage) }
    val scope = rememberCoroutineScope()

    fun onSelectedLanguage(language: Language) {
        selectedLanguage.value = language
    }

    fun onSubmit() {
        scope.launch {
            if (selectedLanguage.value != null) {
                chooseLanguageViewModel.saveLanguage(selectedLanguage.value!!)
                chooseLanguageViewModel.resetLanguage(selectedLanguage.value!!, activity)
                navController.navigate(Routes.CHOOSE_VOICE)
            }
        }
    }

    ChooseLanguageContainer(
        listLanguages = listLanguages,
        onSelectedLanguage = ::onSelectedLanguage,
        selectedLanguage = selectedLanguage.value,
        onSubmit = ::onSubmit
    )
}

@Composable
fun ChooseLanguageContainer(
    listLanguages: List<Language>,
    onSelectedLanguage: ((language: Language) -> Unit) = {},
    selectedLanguage: Language? = null,
    onSubmit: (() -> Unit) = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.background)
            .customSystemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.Choose_Language),
                style = AppTypography.titleLarge
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                onClick = {
                    onSubmit.invoke()
                },
                enabled = selectedLanguage != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = if (selectedLanguage != null) Colors.primary else Colors.surfaceVariant
                )
            }
        }



        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 150.dp)
        ) {
            items(listLanguages) { language ->
                LanguageItem(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp),
                    language = language,
                    isSelected = selectedLanguage?.languageTag == language.languageTag,
                    onSelectedLanguage = { onSelectedLanguage(language) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseLanguagePreview() {
    Scan2HearTheme {
        ChooseLanguageContainer(listLanguages = StaticDataProvider.getLanguages())
    }
}