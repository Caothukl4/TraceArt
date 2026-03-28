package com.aktech.ardrawing.domain.params

import com.aktech.ardrawing.data.remote.model.PageDto

data class PageParams(
//    val pageRef: Int? = null,
    // val currentAudioPosition: Int? = null,
//    val audio: String? = null,
    val ocrText: String? = null,
    val createBy: String? = null,
    val deleted: Boolean? = null,
    val marked: Boolean? = null,
    val order: Int? = null,
    val voice: String? = null,
    val audioData: Map<String, Map<String, String>>? = null,
) {
    fun toPageDto(id: String) = PageDto(
        id = id,
//        pageRef = pageRef,
//        audio = audio,
        ocrText = ocrText,
        createBy = createBy,
        deleted = deleted,
        marked = marked,
        order = order,
        voice = voice,
        audioData = audioData
    )
}