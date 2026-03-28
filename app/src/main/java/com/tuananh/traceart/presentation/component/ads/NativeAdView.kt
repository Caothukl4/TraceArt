package com.tuananh.traceart.presentation.component.ads

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import com.tuananh.traceart.R
import com.tuananh.traceart.domain.model.AdNativeType
import com.tuananh.traceart.domain.model.AdState
import com.tuananh.traceart.presentation.theme.Colors
import com.tuananh.traceart.utils.shimmer

class NativeAdViewController internal constructor(
    internal var loadAd: () -> Unit
)

@Composable
fun rememberNativeAdViewController(): NativeAdViewController {
    return remember { NativeAdViewController({}) }
}

@Composable
fun NativeAdView(
//    onShowNativeAd: (container: ConstraintLayout) -> Unit,
    modifier: Modifier = Modifier,
    adUnitId: String,
    type: AdNativeType = AdNativeType.LARGE,
    trackingName: String? = null,
    controller: NativeAdViewController? = rememberNativeAdViewController()
) {
    val nativeAdViewModel: NativeAdViewModel = hiltViewModel()
    val nativeAdMap by nativeAdViewModel.nativeAdMap.collectAsState()
//    val state by nativeAdViewModel.state.collectAsState()
    val nativeAd by remember {
        derivedStateOf { nativeAdMap[adUnitId]?.nativeAd }
    }

    val state by remember {
        derivedStateOf { nativeAdMap[adUnitId]?.state }
    }

//    val scope = rememberCoroutineScope()
//    LaunchedEffect (Unit) {
//        nativeAdViewModel.load(adUnitId)
//    }
    var firstResumeSkipped by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        controller?.loadAd = {
            nativeAdViewModel.load(adUnitId, trackingName)
        }
    }

    DisposableEffect(Unit) {
//        if (controller == null) {
//            nativeAdViewModel.load(adUnitId, trackingName)
//        }
        onDispose {
//            nativeAdViewModel.stopAutoRefreshNativeAd()
//            nativeAdViewModel.destroyNativeAd(adUnitId)
        }
    }

    LifecycleResumeEffect(Unit) {
        if (firstResumeSkipped) {
//            if (controller == null) {
//                nativeAdViewModel.load(adUnitId, trackingName)
//            }
        } else {
            firstResumeSkipped = true
        }
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if (nativeAd != null) {
            AndroidView(factory = { ctx ->
                val view = LayoutInflater.from(ctx).inflate(
                    if (type == AdNativeType.SMALL) R.layout.layout_ad_native_small else R.layout.layout_ad_native_large,
                    FrameLayout(ctx),
                    false
                ) as NativeAdView
                view
            }, update = { nativeAdView ->
                // 1. Headline
                val headlineView = nativeAdView.findViewById<TextView>(R.id.ad_headline)
                headlineView.text = nativeAd!!.headline
                nativeAdView.headlineView = headlineView

                if (type == AdNativeType.LARGE) {
                    val mediaView = nativeAdView.findViewById<MediaView>(R.id.ad_media)
                    mediaView.setMediaContent(nativeAd!!.mediaContent) // MediaContent từ nativeAd
                    nativeAdView.mediaView = mediaView
                }

                val ctaView = nativeAdView.findViewById<Button>(R.id.ad_call_to_action)
                ctaView.text = nativeAd!!.callToAction
                nativeAdView.callToActionView = ctaView

                if (type == AdNativeType.SMALL) {

                    nativeAd!!.icon?.let { icon ->
                        val iconView = nativeAdView.findViewById<ImageView>(R.id.ad_icon)
                        iconView.setImageDrawable(icon.drawable)
                        nativeAdView.iconView = iconView
                    }

                    nativeAd!!.body?.let {
                        val bodyView = nativeAdView.findViewById<TextView>(R.id.ad_body)
                        bodyView.text = nativeAd!!.body
                    }
                }

                val attributionView = nativeAdView.findViewById<TextView>(R.id.ad_attribution)
                attributionView.visibility = View.VISIBLE
                nativeAdView.advertiserView = attributionView

                nativeAdView.setNativeAd(nativeAd!!)
            })
        }

        if (state == AdState.Loading) {
            ShimmerBox(type = type)
        }
    }

}

@Composable
private fun ShimmerBox(modifier: Modifier = Modifier, type: AdNativeType = AdNativeType.LARGE) {
    // shimmer giả bằng background đổi màu liên tục

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (type == AdNativeType.SMALL) 74.dp else 260.dp)
            .background(
                color = Colors.surfaceVariant, shape = RoundedCornerShape(10.dp)
            )
            .shimmer()

    )
}