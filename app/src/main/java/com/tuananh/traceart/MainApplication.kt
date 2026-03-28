package com.tuananh.traceart

import android.app.Application
import com.tuananh.traceart.startup.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards AppInitializer>
    override fun onCreate() {
        super.onCreate()
        initializers.forEach { it.initialize(this) }
    }
}