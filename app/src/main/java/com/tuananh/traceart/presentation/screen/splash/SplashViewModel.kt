package com.tuananh.traceart.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuananh.traceart.domain.model.AppSetting
import com.tuananh.traceart.presentation.shared.FirebaseSharedState
import com.tuananh.traceart.presentation.shared.SettingSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseSharedState: FirebaseSharedState,
    private val settingSharedState: SettingSharedState,
) : ViewModel() {

    val isReadySetting = settingSharedState.isReady
    val isReadyFirebase = firebaseSharedState.isReady
    val appSetting = settingSharedState.appSetting

    val isReady =  combine(
        isReadySetting,
        isReadyFirebase,
    ) { setting, firebase ->
        setting && firebase
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false,
    )


    fun updateConsentStatusSetting(consentStatus: Int?) {
        viewModelScope.launch {
            val currentAppSetting = appSetting.value ?: AppSetting()
            settingSharedState.saveAppSetting(currentAppSetting.copy(consentStatus = consentStatus))
        }
    }

    fun loadInterstitial(personalizedAds: Boolean){
        viewModelScope.launch {
//            admobShareState.loadInterstitial(StaticDataProvider.AdmobIds.getInterHome(), personalizedAds, "home")
        }
    }

    //    val isReady = firebaseSharedState.isLoaded
//    val isReady: StateFlow<Boolean> = combine(
//        userInfoSharedState.isLoaded,
//        firebaseSharedState.isLoaded,
//        bookSharedState.isLoadedBookSave,
//        settingSharedState.isLoaded,
//    ) { isUserLoaded, isFirebaseConfigLoaded, isBookSaveLoad, isSettingLoad ->
//        isUserLoaded && isFirebaseConfigLoaded && isBookSaveLoad && isSettingLoad
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        false
//    )

}