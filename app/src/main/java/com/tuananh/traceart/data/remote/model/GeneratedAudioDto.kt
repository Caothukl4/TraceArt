package com.tuananh.traceart.data.remote.model

import com.tuananh.traceart.domain.model.GeneratedAudio

data class GeneratedAudioDto(
    val url: String,
) {
    fun toDomain() = GeneratedAudio(url)
}