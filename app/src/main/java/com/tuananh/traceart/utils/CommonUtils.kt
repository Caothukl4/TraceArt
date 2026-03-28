package com.tuananh.traceart.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri


fun Context.getAppVersionCode(): Long {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode // >= API 28
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong() // < API 28
        }
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}


fun Context.getAppVersionName(): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

fun Context.gotoGooglePlayStore(){
    try {
        startActivity(
            Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        )
    } catch (e: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
        )
    }
}

fun Context.getDeviceId(): String? {
    try {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}