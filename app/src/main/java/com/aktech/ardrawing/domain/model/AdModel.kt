package com.aktech.ardrawing.domain.model

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

enum class AdState {
    Loading, Success, Error
}

enum class AdNativeType {
    LARGE, SMALL
}

data class AdNativeData (
    val nativeAd: NativeAd? = null,
    val state: AdState? = null,
    val loadedTime: Long? = null,
    val lastShownTime: Long? = null,
    val retryCount: Int? = null,
)

data class AdInterData(
    val interstitialAd: InterstitialAd? = null,
    val state: AdState? = null,
    val loadedTime: Long? = null,
    val lastShownTime: Long? = null,
    val retryCount: Int? = null,
)