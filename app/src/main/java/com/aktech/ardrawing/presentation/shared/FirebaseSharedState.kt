package com.aktech.ardrawing.presentation.shared

import android.util.Log
import com.aktech.ardrawing.di.ApplicationScope
import com.aktech.ardrawing.domain.model.FirebaseConfig
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseSharedState @Inject constructor(
    commonFirebaseRepository: CommonFirebaseRepository,
    @ApplicationScope appScope: CoroutineScope,
    ) {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    init {
        appScope.launch {
            try {


            val configDeferred = async {
                runCatching { commonFirebaseRepository.getFirebaseConfig() }.getOrElse {
                    Log.e("TAG", "getFirebaseConfig lỗi", it)
                    null
                }
            }
            val idDeferred = async {
                runCatching { commonFirebaseRepository.getInstallationId() }.getOrElse {
                    Log.e("TAG", "getInstallationId lỗi", it)
                    null
                }
            }

            _firebaseConfig.value = configDeferred.await()
            _installationId.value = idDeferred.await()
            } catch (e: Throwable){
                e.printStackTrace()
            } finally {
                _isReady.value = true
            }
        }
    }

    private var _firebaseConfig = MutableStateFlow<FirebaseConfig?>(null)
    val firebaseConfig: StateFlow<FirebaseConfig?> = _firebaseConfig
    private var _installationId = MutableStateFlow<String?>(null)
    val installationId: StateFlow<String?> = _installationId


    fun setFirebaseConfig(config: FirebaseConfig?) {
        _firebaseConfig.value = config
    }

    fun setInstallationId(id: String?) {
        _installationId.value = id
    }
}