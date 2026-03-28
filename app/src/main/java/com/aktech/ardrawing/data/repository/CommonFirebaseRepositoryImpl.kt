package com.aktech.ardrawing.data.repository

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import com.aktech.ardrawing.domain.model.FirebaseConfig
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import com.aktech.ardrawing.utils.getConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonFirebaseRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    CommonFirebaseRepository {
    private val firebaseInitializer = FirebaseInstallations.getInstance()
    private val firebaseAppCheck = FirebaseAppCheck.getInstance()
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val mutex = Mutex()
    override suspend fun getInstallationId(): String =
        withContext(Dispatchers.IO) {
            firebaseInitializer.id.await()
        }

    override suspend fun getAppCheckToken(): String? {
        return mutex.withLock {
            try {
                val result = firebaseAppCheck.getAppCheckToken(false).await()
                result.token
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getFirebaseConfig(): FirebaseConfig {
        return withContext(Dispatchers.IO) {
            try {
                // fetch có thể ném exception nếu không có mạng
                remoteConfig.fetch(0).await()
                remoteConfig.fetchAndActivate().await()
            } catch (e: Exception) {
                Log.e("FirebaseConfig", "Lỗi khi fetch remote config", e)
            }

            // Lúc này dù fetch thất bại, vẫn lấy config cũ đã cache được
            return@withContext remoteConfig.getConfig<FirebaseConfig>()
        }
    }

    override suspend fun getFileStorageUrl(filePath: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val storageRef = firebaseStorage.reference.child(filePath)
                storageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun uploadImage(folderPath: String, localUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Lấy mime type (vd: image/jpeg, image/png)
                val mimeType = context.contentResolver.getType(localUri) ?: "image/jpeg"
                val extension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(mimeType) ?: "jpg"

                // Tạo tên file với timestamp + đuôi mở rộng
                val fileName = "${System.currentTimeMillis()}.$extension"
                val storageRef = firebaseStorage.reference.child("$folderPath/$fileName")

                // Upload file và chờ hoàn thành
                storageRef.putFile(localUri).await()

                // Lấy download URL
                storageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun trackScreens(screenName: String) {
        val bundle = Bundle().apply {
            putString(Param.SCREEN_NAME, screenName)
            putString(Param.SCREEN_CLASS, screenName)
        }
        firebaseAnalytics.logEvent(Event.SCREEN_VIEW, bundle)
    }

    override fun logEvent(eventName: String) {
        firebaseAnalytics.logEvent(eventName, null)
    }
}