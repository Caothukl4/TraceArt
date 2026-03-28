package com.aktech.ardrawing.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties

//data class TtsVoiceInfo(
//    val provider: TtsProvider,
//    val voiceId: String,
//    val name: String?,
//    val gender: Gender,
//    val localeTag: String,
//)

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

enum class TtsProvider {
    ANDROID, OPENAI
}

data class VoiceInfo(
    val voice: String? = null,
    val provider: String? = null,
    val name: String = "",
    val avatar: String = "",
    val sample: String = "",
    val language: Language? = null,
    val ttsVoiceName: String? = null,
//    val languageTag: String? = null
)

@IgnoreExtraProperties
data class Sample(
    val sample: String = ""
)