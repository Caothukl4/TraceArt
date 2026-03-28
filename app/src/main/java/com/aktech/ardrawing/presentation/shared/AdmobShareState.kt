package com.aktech.ardrawing.presentation.shared

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.aktech.ardrawing.di.ApplicationScope
import com.aktech.ardrawing.domain.model.AdInterData
import com.aktech.ardrawing.domain.model.AdNativeData
import com.aktech.ardrawing.domain.model.AdState
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

val TAG = "AdRepositoryImpl"


@Singleton
class AdmobShareState @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val firebaseSharedState: FirebaseSharedState,
    private val globalSharedState: GlobalSharedState,
    private val commonFirebaseRepository: CommonFirebaseRepository
) {

    private val _interstitialMap = MutableStateFlow<Map<String, AdInterData?>>(emptyMap())
    val interstitialMap: StateFlow<Map<String, AdInterData?>> = _interstitialMap

    private val _nativeAdMap = MutableStateFlow<Map<String, AdNativeData?>>(emptyMap())
    val nativeAdMap: StateFlow<Map<String, AdNativeData?>> = _nativeAdMap

    private var autoRefreshJob: Job? = null
    val firebaseConfig = firebaseSharedState.firebaseConfig

    companion object {
        private const val MIN_INTERVAL_MS = 2 * 60 * 1000L // khoảng cách giữa 2 lần show
        private const val MAX_RETRY = 3
        private const val LOAD_TIMEOUT_MS = 10000L // 10s timeout
        private const val INTERSTITIAL_EXPIRATION_MS = 3600_000L
        private const val NATIVE_EXPIRATION_MS = 5 * 60 * 1000L
    }

    private fun updateInterData(key: String, newData: AdInterData) {
        _interstitialMap.update { currentMap ->
            currentMap + (key to newData)
        }
    }

    private fun updateNativeData(key: String, newData: AdNativeData) {
        _nativeAdMap.update { currentMap ->
            currentMap + (key to newData)
        }
    }

//    fun removeNativeAd(key: String) {
//        _nativeAdMap.update { currentMap ->
//            currentMap - key
//        }
//    }

    suspend fun loadInterstitial(
        adUnitId: String,
        personalizedAds: Boolean = true,
        trackingName: String? = null
    ): Boolean =

        withTimeoutOrNull(LOAD_TIMEOUT_MS) {
            suspendCancellableCoroutine<Boolean> { cont ->
                loadInterAd(adUnitId, cont, personalizedAds, trackingName)
            }
        } ?: false

    private fun loadInterAd(
        adUnitId: String,
        cont: CancellableContinuation<Boolean>,
        personalizedAds: Boolean = true,
        trackingName: String? = null
    ) {
        val adInterDataExist = interstitialMap.value[adUnitId]
        if (adInterDataExist?.state == AdState.Loading) {
            cont.resume(false) { cause, _, _ -> }
            return
        }
        updateInterData(
            adUnitId, adInterDataExist?.copy(state = AdState.Loading, interstitialAd = null)
                ?: AdInterData(state = AdState.Loading)
        )
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

        if (trackingName != null) {
            commonFirebaseRepository.logEvent("ad_inter_request_$trackingName")
        }
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d(TAG, "Inter onAdLoaded $adUnitId")
                commonFirebaseRepository.logEvent("ad_inter_matched_$trackingName")
                val adInterData = _interstitialMap.value[adUnitId]!!
                updateInterData(
                    adUnitId,
                    adInterData.copy(
                        state = AdState.Success,
                        retryCount = 0,
                        loadedTime = System.currentTimeMillis(),
                        interstitialAd = ad
                    )
                )
                if (cont.isActive) cont.resume(true) { cause, _, _ -> }
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.d(TAG, "Inter onAdFailedToLoad $adUnitId")
                val adInterData = _interstitialMap.value[adUnitId]!!
                updateInterData(adUnitId, adInterData.copy())
//                retryLoadSuspend(adUnitId, cont)
            }
        })
    }


    fun isInterstitialLoaded(adUnitId: String): Boolean =
        _interstitialMap.value[adUnitId]?.state == AdState.Success

    fun isInterstitialExpired(adUnitId: String): Boolean {
        val loadTime = _interstitialMap.value[adUnitId]?.loadedTime ?: return true
        return System.currentTimeMillis() - loadTime > INTERSTITIAL_EXPIRATION_MS
    }

    suspend fun showInterstitial(
        activity: Activity,
        adUnitId: String,
        minIntervalMs: Long? = null,
        onClosed: () -> Unit,
        trackingName: String? = null
    ) {
        val adInterData = _interstitialMap.value[adUnitId]
        if (adInterData == null) {
            onClosed()
            return
        }
        val interstitialAd = adInterData.interstitialAd
        val now = System.currentTimeMillis()
        val lastShown = adInterData.lastShownTime ?: 0L
        val minIntervalMsOk = minIntervalMs
            ?: (if (firebaseConfig.value != null && firebaseConfig.value?.minIntervalInterSec != null) firebaseConfig.value!!.minIntervalInterSec * 1000 else null)
            ?: MIN_INTERVAL_MS
        val canShow = now - lastShown >= minIntervalMsOk

        if (interstitialAd != null && canShow) {
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdImpression() {
                    Log.d("AdRepositoryImpl", "Inter onAdImpression $adUnitId")
                    if (trackingName != null) {
                        commonFirebaseRepository.logEvent("ad_inter_impression_$trackingName")
                    }
                    val adInterData = _interstitialMap.value[adUnitId]!!
                    updateInterData(
                        adUnitId,
                        adInterData.copy(lastShownTime = System.currentTimeMillis())
                    )
                    super.onAdImpression()
                }

                override fun onAdDismissedFullScreenContent() {
                    globalSharedState.setInterLoading(false)
                    Log.d("AdRepositoryImpl", "Inter onAdDismissedFullScreenContent $adUnitId")
                    val adInterData = _interstitialMap.value[adUnitId]!!
                    updateInterData(adUnitId, adInterData.copy(interstitialAd = null))
                    appScope.launch(Dispatchers.Main) {
                        loadInterstitial(adUnitId)
                    }
                    onClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    globalSharedState.setInterLoading(false)
                    Log.d(
                        "AdRepositoryImpl",
                        "Inter onAdFailedToShowFullScreenContent $adUnitId $adError"
                    )
                    val adInterData = _interstitialMap.value[adUnitId]!!
                    updateInterData(adUnitId, adInterData.copy(interstitialAd = null))
                    appScope.launch(Dispatchers.Main) {
                        loadInterstitial(adUnitId)
                    }
                    onClosed()
                }
            }
            globalSharedState.setInterLoading(true)
            delay(1000L)

            interstitialAd.show(activity)
        } else {
            if (interstitialAd == null) {
                appScope.launch(Dispatchers.Main) {
                    loadInterstitial(adUnitId)
                }
            }
            onClosed()
        }
    }


    suspend fun loadNative(
        adUnitId: String,
        personalizedAds: Boolean = true,
        trackingName: String? = null
    ): Boolean =
        withTimeoutOrNull(LOAD_TIMEOUT_MS) {
            suspendCancellableCoroutine<Boolean> { cont ->
                loadNativeAd(adUnitId, cont, personalizedAds, trackingName)
            }
        } ?: false

    private fun loadNativeAd(
        adUnitId: String,
        cont: CancellableContinuation<Boolean>,
        personalizedAds: Boolean = true,
        trackingName: String? = null
    ) {
        val adInterDataExist = _nativeAdMap.value[adUnitId]
        if (adInterDataExist?.state == AdState.Loading) {
            cont.resume(false) { cause, _, _ -> }
            return
        }
        if (adInterDataExist?.nativeAd != null) {
            adInterDataExist.nativeAd.destroy()
        }
        updateNativeData(
            adUnitId,
            adInterDataExist?.copy(state = AdState.Loading, nativeAd = null) ?: AdNativeData(
                state = AdState.Loading
            )
        )
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                val nativeAd = _nativeAdMap.value[adUnitId]!!
                updateNativeData(
                    adUnitId,
                    nativeAd.copy(
                        state = AdState.Success,
                        retryCount = 0,
                        loadedTime = System.currentTimeMillis(),
                        nativeAd = ad
                    )
                )
                if (cont.isActive) cont.resume(true) { cause, _, _ -> }
            }
            .withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    if (trackingName != null) {
                        commonFirebaseRepository.logEvent("ad_native_matched_$trackingName")
                        Log.d(TAG, "Native onAdLoaded $trackingName")

                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
//                        onAdLoaded(AdState.Error)
                    Log.d(TAG, "Native onAdFailedToLoad $adUnitId $error")
//                        _nativeAdMap.value = _nativeAdMap.value.filter { it.key != adUnitId }
                    val nativeAd = _nativeAdMap.value[adUnitId]!!
                    updateNativeData(adUnitId, nativeAd.copy(state = AdState.Error))
//                        retryNativeAd(adUnitId, cont)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    if (trackingName != null) {
                        commonFirebaseRepository.logEvent("ad_native_impression_$trackingName")
                    }
                    Log.d(TAG, "Native onAdImpression $adUnitId")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "Native onAdClicked $adUnitId")
                }
            })
            .build()
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
        if (trackingName != null) {
            commonFirebaseRepository.logEvent("ad_native_request_$trackingName")
            Log.d(TAG, "Native request $trackingName")
        }
        adLoader.loadAd(adRequest)
    }

//    private fun retryNativeAd(
//        adUnitId: String,
//        cont: CancellableContinuation<Boolean>
//    ) {
//        val nativeAd = _nativeAdMap.value[adUnitId]
//        if (nativeAd == null) {
//            if (cont.isActive) cont.resume(false) { cause, _, _ -> }
//            return
//        }
//        val retryCount = nativeAd.retryCount ?: 0
//        if (retryCount >= MAX_RETRY) {
//            if (cont.isActive) cont.resume(false) { cause, _, _ -> }
//            return
//        }
//        val delayTime = retryCount * 3000L
//        Handler(Looper.getMainLooper()).postDelayed({
////            retryCountMap[adUnitId] = retryCount + 1
//            val nativeAd = _nativeAdMap.value[adUnitId]!!
//            updateNativeData(adUnitId, nativeAd.copy(retryCount = retryCount + 1))
//            loadNativeAd(adUnitId, cont)
//        }, delayTime)
//    }


    fun isNativeAdExpired(adUnitId: String): Boolean {
        val loadTime = _nativeAdMap.value[adUnitId]?.loadedTime ?: return true
        val nativeExpirationSecOk =
            (if (firebaseConfig.value != null && firebaseConfig.value?.nativeExpirationSec != null) firebaseConfig.value!!.nativeExpirationSec * 1000 else null)
                ?: NATIVE_EXPIRATION_MS
        return System.currentTimeMillis() - loadTime > nativeExpirationSecOk
    }
}