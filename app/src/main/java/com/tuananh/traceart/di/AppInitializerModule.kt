package com.tuananh.traceart.di

import com.tuananh.traceart.startup.AppInitializer
import com.tuananh.traceart.startup.FirebaseInitializer
import com.tuananh.traceart.startup.LocaleInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInitializerModule {

    @Binds
    @IntoSet
    abstract fun bindLocaleInitializer(
        impl: LocaleInitializer
    ): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindFirebaseInitializer(
        impl: FirebaseInitializer
    ): AppInitializer
}