package com.aktech.ardrawing.di

import com.aktech.ardrawing.startup.AppInitializer
import com.aktech.ardrawing.startup.FirebaseInitializer
import com.aktech.ardrawing.startup.LocaleInitializer
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