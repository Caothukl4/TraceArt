package com.aktech.ardrawing.data.remote.model

import com.aktech.ardrawing.domain.model.VoiceInfo
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class VoiceInfoDto(
    val voice: String = "",
    val provider: String = "",
    val locale: Map<String, LocaleData> = mutableMapOf(),
    val order: Int = 0
) {
    fun toDomain(languageTag: String): VoiceInfo {
       val languageCode =languageTag.take(2)
        return VoiceInfo(
            voice = voice,
            provider = provider,
            name = locale[languageTag]?.name ?: locale[languageCode]?.name?: "",
            avatar = locale[languageTag]?.avatar ?: locale[languageCode]?.avatar?: ""
        )
    }
}

data class LocaleData(
    val name: String = "",
    val avatar: String = "",
)