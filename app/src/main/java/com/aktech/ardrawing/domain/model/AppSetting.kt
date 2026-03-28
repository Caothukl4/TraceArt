package com.aktech.ardrawing.domain.model

data class AppSetting(
    val languageTag: String? = null,
    val voice: String? = null,
    val firstInstall: Boolean? = null,
    val consentStatus: Int? = null,
    val listenedDuration: Long? = null,
)
