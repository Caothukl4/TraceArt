package com.tuananh.traceart.di

import android.content.Context
import com.tuananh.traceart.data.local.preferences.SettingPreferences
import com.tuananh.traceart.data.local.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorePreferencesModule {

    @Provides
    @Singleton
    fun provideSettingPreferences(@ApplicationContext context: Context): SettingPreferences {
        return SettingPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}