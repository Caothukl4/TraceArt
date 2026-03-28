package com.aktech.ardrawing.startup

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.aktech.ardrawing.data.local.preferences.SettingPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

class LocaleInitializer @Inject constructor(
    private val preferences: SettingPreferences
) : AppInitializer {

    override fun initialize(application: Application) {
        val appSetting = runBlocking { preferences.getAppSetting().first() }
        val locale = appSetting?.languageTag?.let(::Locale) ?: Locale.getDefault()
        val appLocale = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}