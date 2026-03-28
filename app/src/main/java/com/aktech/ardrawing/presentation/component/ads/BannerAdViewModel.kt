package com.aktech.ardrawing.presentation.component.ads

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aktech.ardrawing.domain.model.AdState
import com.aktech.ardrawing.presentation.shared.SettingSharedState
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.ump.ConsentInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

val TAG = "BannerAdViewModel"

@HiltViewModel
class BannerAdViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingSharedState: SettingSharedState

) : ViewModel() {

    private val _adView = MutableStateFlow<AdView?>(null)
    val adView: StateFlow<AdView?> = _adView

    private val _state = MutableStateFlow(AdState.Loading)
    val state: StateFlow<AdState> = _state
    val appSetting = settingSharedState.appSetting

    fun loadAdview(adUnit: String, screenWidthDp: Int) {
        if (_adView.value != null) return
        _adView.value = AdView(context).apply {
            setAdSize(
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    screenWidthDp
                )
            )
            adUnitId = adUnit
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "Banner onAdLoaded $adUnit")
                    _state.value = AdState.Success
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(TAG, "Banner onAdFailedToLoad $adUnit")
                    _state.value = AdState.Error
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Banner onAdImpression $adUnit")
                    super.onAdImpression()
                }
            }
            val personalizedAds =
                appSetting.value?.consentStatus == ConsentInformation.ConsentStatus.OBTAINED
                        || appSetting.value?.consentStatus == ConsentInformation.ConsentStatus.NOT_REQUIRED
            val adRequest: AdRequest =
                if (personalizedAds) {
                    AdRequest.Builder().build()
                } else {
                    val extras = Bundle()
                    extras.putString("npa", "1")
                    AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                        .build()
                }
            loadAd(adRequest)
        }
    }
}