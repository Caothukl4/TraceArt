package com.aktech.ardrawing.presentation.component.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktech.ardrawing.presentation.shared.AdmobShareState
import com.aktech.ardrawing.presentation.shared.SettingSharedState
import com.google.android.ump.ConsentInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NativeAdViewModel @Inject constructor(
    private val admobShareState: AdmobShareState,
    private val settingSharedState: SettingSharedState,

    ) :
    ViewModel() {
    val nativeAdMap = admobShareState.nativeAdMap
    val appSetting = settingSharedState.appSetting
    fun load(adUnitId: String, trackingName: String? = null) {
        viewModelScope.launch {
            if (nativeAdMap.value[adUnitId]?.nativeAd == null || admobShareState.isNativeAdExpired(
                    adUnitId
                )
            ) {
                val personalizedAds = appSetting.value?.consentStatus == null ||
                        appSetting.value?.consentStatus == ConsentInformation.ConsentStatus.OBTAINED
                        || appSetting.value?.consentStatus == ConsentInformation.ConsentStatus.NOT_REQUIRED
                admobShareState.loadNative(adUnitId, personalizedAds, trackingName)
            }
        }
    }
}