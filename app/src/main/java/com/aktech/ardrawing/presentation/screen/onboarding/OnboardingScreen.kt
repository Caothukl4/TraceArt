package com.aktech.ardrawing.presentation.screen.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aktech.ardrawing.R
import com.aktech.ardrawing.data.local.provider.StaticDataProvider
import com.aktech.ardrawing.domain.model.AdNativeType
import com.aktech.ardrawing.presentation.component.ScreenContainer
import com.aktech.ardrawing.presentation.component.UpdateSystemBars
import com.aktech.ardrawing.presentation.component.ads.NativeAdView
import com.aktech.ardrawing.presentation.component.ads.rememberNativeAdViewController
import com.aktech.ardrawing.presentation.component.button.Button
import com.aktech.ardrawing.presentation.component.button.ButtonSize
import com.aktech.ardrawing.presentation.navigation.LocalNavController
import com.aktech.ardrawing.presentation.navigation.Routes
import com.aktech.ardrawing.presentation.theme.Colors
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen() {
    UpdateSystemBars(useLightBars = true)
    val navController = LocalNavController.current
    val pagerState = rememberPagerState(pageCount = { 3 }) // 3 page demo
    val  scope = rememberCoroutineScope()
    val listPage = arrayListOf<Int>(
        R.drawable.img_onboarding_1,
        R.drawable.img_onboarding_2,
        R.drawable.img_onboarding_3
    )
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val listLogging = arrayListOf("Onboarding_1", "Onboarding_2", "Onboarding_3")
    val listAds = arrayListOf(
        StaticDataProvider.AdmobIds.getNativeOnBoarding1(),
        StaticDataProvider.AdmobIds.getNativeOnBoarding2(),
        StaticDataProvider.AdmobIds.getNativeOnBoarding3()
    )

    fun onClickNext(){
        scope.launch {
            if(pagerState.currentPage == 2){
                navController.navigate(Routes.CHOOSE_LANGUAGE)
            } else {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    ScreenContainer(
        backgroundColor = Colors.surface,
        disableBackButton = true,
        safeBottom = true,
        hideHeader = true
    ) {
        // Pager
        Box(modifier = Modifier.fillMaxSize()) {

            HorizontalPager(
                state = pagerState,
            ) { page ->
                Column(modifier = Modifier.fillMaxSize()) {

                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentDescription = null,
                        painter = painterResource(listPage[page])
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    LazyNativeAdView(
                        currentPage = pagerState.currentPage,
                        page = page, // chỉ true khi focus
                        adUnitId = listAds[page],
                        type = AdNativeType.LARGE,
                        trackingName = listLogging[page],
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp)
                    )

                }

            }
            Row(
                modifier = Modifier
                    .padding(top = (screenWidthDp + 20).dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(if (isSelected) 20.dp else 8.dp)
                                .height(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) Colors.primary
                                    else Color.Gray.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
                Button(text =  "Next", size = ButtonSize.Small, onClick = ::onClickNext)
            }
//        Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun LazyNativeAdView(
    currentPage: Int,
    page: Int,
    adUnitId: String,
    type: AdNativeType,
    trackingName: String,
    modifier: Modifier = Modifier
) {
    Log.d("LazyNativeAdView", "currentPage: $currentPage, page: $page")
    // Đánh dấu ad đã được tạo hay chưa
    var created by rememberSaveable { mutableStateOf(false) }
    val nativeAdViewController = rememberNativeAdViewController()
    // Khi show = true lần đầu → set created = true

    NativeAdView(
        controller = nativeAdViewController,
        modifier = modifier,
        adUnitId = adUnitId,
        type = type,
        trackingName = trackingName
    )

    LaunchedEffect(currentPage) {
        Log.d("LazyNativeAdView LaunchedEffect", "currentPage: $currentPage, page: $page")

        if (currentPage == page) {
            nativeAdViewController.loadAd()
        }
    }

}