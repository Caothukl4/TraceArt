package com.tuananh.traceart.domain.model

import com.tuananh.traceart.utils.toSha1

data class Page(
    val id: String = "",
    val ocrText: String? = null,
    val createAt: Long? = null,
    val createBy: String? = null,
    val updateAt: Long? = null,
    val deleted: Boolean? = null,
    val marked: Boolean? = null,
    val order: Int? = null,
    val voice: String? = null,
    val generatingType: GeneratingType? = null,
    val audioData: Map<String, Map<String, String>>? = null,
) {
    fun toPlayingPage(): PlayingPage {
        return PlayingPage(
            id = id,
            audio = audioData?.get(ocrText?.toSha1())?.get(voice),
            voice = voice
        )
    }
}

data class PlayingPage(
    val id: String = "",
    val audio: String? = null,
    val voice: String? = null,
)