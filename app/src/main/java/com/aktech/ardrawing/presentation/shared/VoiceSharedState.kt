package com.aktech.ardrawing.presentation.shared

import com.aktech.ardrawing.domain.model.VoiceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceSharedState @Inject constructor() {
    private val _listVoice = MutableStateFlow<List<VoiceInfo>>(emptyList())
    val listVoices: StateFlow<List<VoiceInfo>> = _listVoice

    fun setListVoice(voices: List<VoiceInfo>) {
        _listVoice.value = voices
    }
}