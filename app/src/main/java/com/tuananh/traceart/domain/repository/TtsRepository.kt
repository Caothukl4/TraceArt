package com.tuananh.traceart.domain.repository

import android.speech.tts.Voice
import java.io.File

interface TtsRepository {
    suspend fun getAvailableVoicesByLocale(localeTag: String): List<Voice>
    suspend fun textToAudioFile(
        text: String,
        ttsVoiceName: String? = null,
    ): File?

    fun speak(
        text: String,
        voiceIn: Voice,
        onDone: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
    )
}