package com.tuananh.traceart.presentation.shared

import com.tuananh.traceart.data.local.preferences.UserPreferences
import com.tuananh.traceart.di.ApplicationScope
import com.tuananh.traceart.domain.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoSharedState @Inject constructor(
    private val userPreferences: UserPreferences,
    @ApplicationScope private val appScope: CoroutineScope
) {


    val userInfo: StateFlow<UserInfo?> = userPreferences
        .getUserInfo()
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

     fun updateUserInfo(info: UserInfo) {
         appScope.launch {
             userPreferences.saveUserInfo(info)
         }
    }
    fun clear(){
        appScope.launch {
            userPreferences.clear()
        }
    }
}