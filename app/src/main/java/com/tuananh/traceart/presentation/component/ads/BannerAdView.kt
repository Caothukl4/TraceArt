package com.tuananh.traceart.presentation.component.ads

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuananh.traceart.domain.model.AdState
import com.tuananh.traceart.presentation.theme.Colors
import com.tuananh.traceart.utils.customSystemBarsPadding
import com.tuananh.traceart.utils.shimmer
import com.google.android.gms.ads.AdSize

object BannerSizes {
    @Suppress("VisibleForTests")
    fun banner(): AdSize = AdSize.BANNER

    @Suppress("VisibleForTests")
    fun adaptive(context: Context, widthPx: Int): AdSize =
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, widthPx)
}

@Composable
private fun ShimmerBox(modifier: Modifier = Modifier) {
    // shimmer giả bằng background đổi màu liên tục

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = Colors.surface,
            )
            .shimmer()

    )
}

@Composable
fun BannerAdView(
    adUnit: String,
    modifier: Modifier = Modifier,
    safeBottom: Boolean = true,
) {
    val bannerAdViewModel: BannerAdViewModel = hiltViewModel()

    val state by bannerAdViewModel.state.collectAsState()
    val adView by bannerAdViewModel.adView.collectAsState()

    // Xác định độ rộng sử dụng: nếu widthDp không có thì lấy width của màn hình
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(Unit) {
        bannerAdViewModel.loadAdview(adUnit, screenWidthDp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .customSystemBarsPadding(top = false, bottom = safeBottom)
            .padding(top = 4.dp, bottom = 2.dp)
    ) {
        adView?.let {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { ctx ->
                    adView!!
                }
            )
        }


        when (state) {
            AdState.Loading -> {
                ShimmerBox(
                )
            }

            AdState.Error -> {
                // Ẩn toàn bộ nếu lỗi
                Spacer(modifier = Modifier.height(0.dp))
            }

            AdState.Success -> {
                // Không cần gì thêm, vì AdView đã hiển thị
            }
        }
    }
}