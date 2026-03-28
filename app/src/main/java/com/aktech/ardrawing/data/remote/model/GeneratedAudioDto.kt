package com.aktech.ardrawing.data.remote.model

import com.aktech.ardrawing.domain.model.GeneratedAudio

data class GeneratedAudioDto(
    val url: String,
) {
    fun toDomain() = GeneratedAudio(url)
}