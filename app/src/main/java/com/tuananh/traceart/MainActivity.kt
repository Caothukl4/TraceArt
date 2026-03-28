package com.tuananh.traceart

import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.tuananh.traceart.presentation.component.SystemBars
import com.tuananh.traceart.presentation.component.loading.GlobalInterLoading
import com.tuananh.traceart.presentation.component.loading.GlobalLoading
import com.tuananh.traceart.presentation.component.snackbar.GlobalSnackbar
import com.tuananh.traceart.presentation.navigation.AppNavHost
import com.tuananh.traceart.presentation.navigation.LocalNavController
import com.tuananh.traceart.presentation.theme.Scan2HearTheme
import dagger.hilt.android.AndroidEntryPoint

val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private lateinit var consentInformation: ConsentInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        MobileAds.initialize(this) {}
//        FirebaseApp.initializeApp(this)
//        val testDeviceIds = listOf("B3EEABB8EE11C2BE770B684D95219ECB")
//        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
//        MobileAds.setRequestConfiguration(configuration)

        setContent {
            val navController = rememberNavController()
            SystemBars(true)
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalNavController provides navController
            ) {
                Scan2HearTheme {
                    AppNavHost(
                        navController = navController,
                    )
                    GlobalSnackbar()
                    GlobalLoading()
                    GlobalInterLoading()
                }
            }
        }
    }


}

