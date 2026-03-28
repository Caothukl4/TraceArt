package com.aktech.ardrawing.data.manager

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class TtsManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val onReadyQueue = mutableListOf<() -> Unit>()
    private val continuationMap = ConcurrentHashMap<String, Continuation<File?>>()

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        isInitialized = status == TextToSpeech.SUCCESS
        onReadyQueue.forEach { it() }
        onReadyQueue.clear()
    }

    private fun runWhenReady(action: () -> Unit) {
        if (isInitialized) action() else onReadyQueue.add(action)
    }

    suspend fun textToAudioFile(
        text: String,
        ttsVoiceName: String? = null,
    ): File? = suspendCoroutine { continuation ->
        runWhenReady {
            tts?.apply {
                voice = tts?.voices?.find { it.name == ttsVoiceName }
                val outputFile = File(context.filesDir, "tts_output_${System.currentTimeMillis()}")
                val utteranceId = "tts_output_${System.currentTimeMillis()}"

                val params = Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
                }

                continuationMap[utteranceId] = continuation

                setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        continuationMap.remove(utteranceId)?.resume(outputFile)
                    }

                    @Deprecated("Deprecated in Java",
                        ReplaceWith("continuation.resume(null)", "kotlin.coroutines.resume")
                    )
                    override fun onError(utteranceId: String?) {
                        continuationMap.remove(utteranceId)?.resume(null)
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        continuationMap.remove(utteranceId)?.resume(null)
                    }
                })

                // Bắt đầu synthesize
                synthesizeToFile(text, params, outputFile, utteranceId)
            } ?: continuation.resume(null)
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    fun speak(
        text: String,
        voiceIn: Voice,
        onDone: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
    ) {
        runWhenReady {
            tts?.apply {
                voice = voiceIn
                TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED
                // Gắn listener để biết khi nào đọc xong
                setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // Bắt đầu đọc
                    }

                    override fun onDone(utteranceId: String?) {
                        onDone?.invoke()
                    }

                    @Deprecated("Deprecated in Java", ReplaceWith("onError?.invoke()"))
                    override fun onError(utteranceId: String?) {
                        onError?.invoke()
                    }

                    // Dành cho Android >= 21
                    override fun onError(utteranceId: String?, errorCode: Int) {
                        onError?.invoke()
                    }
                })

                val params = Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "tts_speak")
                }

                speak(text, TextToSpeech.QUEUE_FLUSH, params, "tts_speak")
            }
        }
    }


    suspend fun getAvailableVoicesByLocale(localeTag: String): List<Voice> =
        suspendCoroutine { cont ->
            runWhenReady {
                val locale = Locale.forLanguageTag(localeTag)

                val voices = tts?.voices ?: emptyList()
                val listVoices: List<Voice>
                val regionMatchedVoices = voices.filter {
                   it.locale.language.take(2) == locale.language
                }.sortedWith(
                    compareByDescending<Voice> { it.locale == locale }
                        .thenByDescending { it.quality }
                        .thenByDescending { it.isNetworkConnectionRequired }
                )
                listVoices = regionMatchedVoices.take(2)
                cont.resume(listVoices)

            }
        }
}