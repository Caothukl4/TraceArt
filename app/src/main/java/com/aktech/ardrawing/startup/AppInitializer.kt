package com.aktech.ardrawing.startup

import android.app.Application

interface AppInitializer {
    fun initialize(application: Application)
}