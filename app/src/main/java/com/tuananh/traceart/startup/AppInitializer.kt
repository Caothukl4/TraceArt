package com.tuananh.traceart.startup

import android.app.Application

interface AppInitializer {
    fun initialize(application: Application)
}