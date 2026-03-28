package com.tuananh.traceart.presentation.shared

import com.tuananh.traceart.data.local.preferences.SettingPreferences
import com.tuananh.traceart.di.ApplicationScope
import com.tuananh.traceart.domain.model.AppSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingSharedState @Inject constructor(
    private val settingPreferences: SettingPreferences,
    @ApplicationScope private val appScope: CoroutineScope
) {
    private val _appSetting = MutableStateFlow<AppSetting?>(null)
    val appSetting: StateFlow<AppSetting?> = _appSetting
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    init {
        appScope.launch {
            val firstSetting = settingPreferences.getAppSetting().first()
            _appSetting.value = firstSetting
            _isReady.value = true
            settingPreferences.getAppSetting().drop(1).collect { _newAppSetting ->
                if (_newAppSetting != _appSetting.value) {
                    _appSetting.value = _newAppSetting
                }
            }
        }
    }
//    private val defaultLanguageTag: String = Locale.getDefault().toLanguageTag()

//    val languageTag: StateFlow<String> =
//        appSetting.map { it?.languageTag ?: defaultLanguageTag }.stateIn(
//            scope = appScope,
//            started = SharingStarted.Eagerly,
//            initialValue = defaultLanguageTag
//        )



    val savedVoice = appSetting.map {
        it?.voice
    }.stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    suspend fun saveAppSetting(setting: AppSetting) {
        settingPreferences.saveAppSetting(setting)
        _appSetting.value = setting
    }

    suspend fun clearAppSetting() {
        settingPreferences.clear()
    }
}