package com.aktech.ardrawing.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.aktech.ardrawing.domain.model.AppSetting
import com.aktech.ardrawing.utils.getData
import com.aktech.ardrawing.utils.saveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

val Context.settingDataStore by preferencesDataStore("setting")

@Singleton
class SettingPreferences @Inject constructor (@ApplicationContext private val context: Context) {

//    companion object {
//        val LOCALE_CODE = stringPreferencesKey("LOCALE_CODE")
//    }
//
//    val localeFlow = context.settingDataStore.data
//        .map { prefs -> prefs[LOCALE_CODE]?.let(::Locale) ?: Locale.getDefault() }

//
//    suspend fun setLocale(lang: String) {
//        context.settingDataStore.edit { prefs ->
//            prefs[LOCALE_CODE] = lang
//        }
//    }


    fun getAppSetting(): Flow<AppSetting?> {
        return context.settingDataStore.getData<AppSetting>()
    }

    suspend fun saveAppSetting(appSetting: AppSetting) {
        context.settingDataStore.saveData(appSetting)
    }

    suspend fun clear() {
        context.settingDataStore.edit { it.clear() }
    }
}