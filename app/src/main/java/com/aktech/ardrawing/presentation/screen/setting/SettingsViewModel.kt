package com.aktech.ardrawing.presentation.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktech.ardrawing.data.local.provider.StaticDataProvider
import com.aktech.ardrawing.domain.model.AppSetting
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import com.aktech.ardrawing.presentation.shared.SettingSharedState
import com.aktech.ardrawing.presentation.shared.VoiceSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingSharedState: SettingSharedState,
    private val voiceSharedState: VoiceSharedState,
    private val commonFirebaseRepository: CommonFirebaseRepository
) :
    ViewModel() {
    private val savedVoice = settingSharedState.savedVoice
    private val listVoices = voiceSharedState.listVoices
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

    val selectedVoice = combine(
        listVoices,
        savedVoice
    ) { list, selected ->
        list.find { it.voice == selected } ?: list.firstOrNull()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun saveVoice(voice: String) {
        viewModelScope.launch {
            val currentAppSetting = appSetting.value ?: AppSetting()
            settingSharedState.saveAppSetting(
                currentAppSetting.copy(
                    voice = voice,
                    firstInstall = false
                )
            )
        }
    }

    fun updateConsentStatusSetting(consentStatus: Int?) {
        viewModelScope.launch {
            val currentAppSetting = appSetting.value ?: AppSetting()
            settingSharedState.saveAppSetting(currentAppSetting.copy(consentStatus = consentStatus))
        }
    }

    fun logEvent(eventName: String){
        commonFirebaseRepository.logEvent(eventName)
    }
}