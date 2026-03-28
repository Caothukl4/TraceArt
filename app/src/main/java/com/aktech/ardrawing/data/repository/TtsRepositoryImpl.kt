package com.aktech.ardrawing.data.repository

import android.speech.tts.Voice
import com.aktech.ardrawing.data.manager.TtsManager
import com.aktech.ardrawing.domain.repository.TtsRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsRepositoryImpl @Inject constructor(
   private val ttsManager: TtsManager
): TtsRepository {
    override suspend fun getAvailableVoicesByLocale(localeTag: String): List<Voice> {
        return ttsManager.getAvailableVoicesByLocale(localeTag)
    }

    override suspend fun textToAudioFile(text: String, ttsVoiceName: String?): File? {
        return ttsManager.textToAudioFile(text, ttsVoiceName)
    }

    override fun speak(
        text: String,
        voiceIn: Voice,
        onDone: (() -> Unit)?,
        onError: (() -> Unit)?
    ) {
        return ttsManager.speak(text, voiceIn, onDone, onError)
    }
}