package com.aktech.ardrawing.presentation.screen.splash

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aktech.ardrawing.R
import com.aktech.ardrawing.presentation.navigation.LocalNavController
import com.aktech.ardrawing.presentation.navigation.Routes
import com.aktech.ardrawing.presentation.theme.AppTypography
import com.aktech.ardrawing.presentation.theme.Colors
import com.aktech.ardrawing.presentation.theme.Scan2HearTheme
import com.aktech.ardrawing.utils.requestUserConsent
import com.google.android.ump.ConsentInformation

@Composable
fun SplashScreen() {
    val navController = LocalNavController.current
    val activity = LocalActivity.current
    val splashViewModel: SplashViewModel = hiltViewModel()
    val isReady by splashViewModel.isReady.collectAsState()
    val appSetting by splashViewModel.appSetting.collectAsState()

    LaunchedEffect(isReady) {
        if (isReady) {
            var consentStatus = appSetting?.consentStatus
            if (appSetting?.consentStatus == null) {
                val consentInformation = activity?.requestUserConsent()
                consentStatus = consentInformation?.consentStatus
                splashViewModel.updateConsentStatusSetting(
                    consentInformation?.consentStatus
                )
            }
            val personalizedAds =
                consentStatus == null || consentStatus == ConsentInformation.ConsentStatus.OBTAINED || consentStatus == ConsentInformation.ConsentStatus.NOT_REQUIRED
            splashViewModel.loadInterstitial(personalizedAds)

            if (appSetting?.firstInstall == null || appSetting?.firstInstall == true) {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(0) { inclusive = true } // Xóa toàn bộ back stack
                    launchSingleTop = true
                }
            } else {
                navController.navigate(Routes.MAIN) {
                    popUpTo(0) { inclusive = true } // Xóa toàn bộ back stack
                    launchSingleTop = true
                }
            }
        }
    }
    SplashScreenPreview()
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    Scan2HearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.surface)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(180.dp),
                painter = painterResource(R.drawable.ic_launcher_round),
                contentDescription = "Logo",
                contentScale = ContentScale.FillBounds
            )
            Text(modifier = Modifier.padding(bottom =50.dp).align (Alignment.BottomCenter), text = "Loading...", style = AppTypography.labelMedium)
        }
    }

}