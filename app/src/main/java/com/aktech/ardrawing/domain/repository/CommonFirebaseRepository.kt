package com.aktech.ardrawing.domain.repository

import android.net.Uri
import com.aktech.ardrawing.domain.model.FirebaseConfig

interface CommonFirebaseRepository {
    suspend fun getInstallationId(): String?

    suspend fun getAppCheckToken(): String?

    suspend fun getFirebaseConfig(): FirebaseConfig?

    suspend fun getFileStorageUrl(filePath: String): String?

    suspend fun uploadImage(folderPath: String, localUri: Uri): String?

    fun trackScreens(screenName: String)

    fun logEvent(eventName: String)

}