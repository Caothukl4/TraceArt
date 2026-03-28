package com.tuananh.traceart.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.tuananh.traceart.domain.model.UserInfo
import com.tuananh.traceart.utils.getData
import com.tuananh.traceart.utils.saveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

val Context.userDataStore by preferencesDataStore("user")

@Singleton
class UserPreferences @Inject constructor (@ApplicationContext private val context: Context) {

    suspend fun saveUserInfo(user: UserInfo) {
        context.userDataStore.saveData(user)
    }

    fun getUserInfo(): Flow<UserInfo?> {
        return context.userDataStore.getData<UserInfo>()
    }

    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
}