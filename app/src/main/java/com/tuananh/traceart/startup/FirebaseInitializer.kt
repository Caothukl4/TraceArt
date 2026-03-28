package com.tuananh.traceart.startup

import android.app.Application
import com.tuananh.traceart.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.InternalDebugSecretProvider
import com.google.firebase.appcheck.debug.internal.DebugAppCheckProvider
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.inject.Provider
import java.util.concurrent.Executors
import javax.inject.Inject

class FirebaseInitializer @Inject constructor() : AppInitializer {
    override fun initialize(application: Application) {
        if (BuildConfig.DEBUG) {
            FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(CustomAppCheckProviderFactory(), true)
        } else {
//            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
//                DebugAppCheckProviderFactory.getInstance()
//            )
            FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
        }
    }

}

class CustomAppCheckProviderFactory : AppCheckProviderFactory {
    private val executor = Executors.newSingleThreadExecutor()
    override fun create(firebaseApp: FirebaseApp): AppCheckProvider {
        return DebugAppCheckProvider(firebaseApp, Provider { CustomDebugSecretProvider() },
            executor, executor, executor)
    }
}

class CustomDebugSecretProvider : InternalDebugSecretProvider {
    override fun getDebugSecret(): String = "57EA52D7-D723-49DA-B7D7-9FBCEA5711A2"
}