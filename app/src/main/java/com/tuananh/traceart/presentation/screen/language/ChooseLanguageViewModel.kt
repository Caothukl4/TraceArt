package com.tuananh.traceart.presentation.screen.language

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuananh.traceart.data.local.provider.StaticDataProvider
import com.tuananh.traceart.domain.model.AppSetting
import com.tuananh.traceart.domain.model.Language
import com.tuananh.traceart.domain.repository.CommonFirebaseRepository
import com.tuananh.traceart.presentation.shared.SettingSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChooseLanguageViewModel @Inject constructor(
    private val settingSharedState: SettingSharedState,
    private val commonFirebaseRepository: CommonFirebaseRepository
) : ViewModel() {

    private val _listLanguages = MutableStateFlow<List<Language>>(emptyList())
    val listLanguages: StateFlow<List<Language>> = _listLanguages
    val appSetting = settingSharedState.appSetting

    val savedLanguage =
        appSetting.map { setting ->
            StaticDataProvider.getLanguages().find { language ->
                language.languageTag == (setting?.languageTag ?: Locale.getDefault()
                    .toLanguageTag())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        val localeDefault = Locale.getDefault()
        _listLanguages.value = StaticDataProvider.getLanguages().sortedWith(
            compareBy(
                {
                    Locale.forLanguageTag(it.languageTag)
                        .toLanguageTag() != localeDefault.toLanguageTag()
                },
                { Locale.forLanguageTag(it.languageTag).language != localeDefault.language },
            )
        )
    }

    suspend fun saveLanguage(language: Language) {
        val currentAppSetting = appSetting.value ?: AppSetting()
        settingSharedState.saveAppSetting(currentAppSetting.copy(languageTag = language.languageTag))
        commonFirebaseRepository.logEvent("changed_language")
    }

    fun resetLanguage(language: Language, activity: Activity?) {
        val appLocale = LocaleListCompat.create(language.toLocale())
        AppCompatDelegate.setApplicationLocales(appLocale)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            activity?.recreate()
        }
    }

}