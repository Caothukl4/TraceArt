package com.tuananh.traceart.data.remote.model

import com.tuananh.traceart.domain.model.Page
import com.google.firebase.Timestamp

data class PageDto(
    var id: String = "",
//    val pageRef: Int? = null,
    // val currentAudioPosition: Int? = null,
//    val audio: String? = null,
    val ocrText: String? = null,
    val createAt: Timestamp? = null,
    val createBy: String? = null,
    val updateAt: Timestamp? = null,
    val deleted: Boolean? = null,
    val marked: Boolean? = null,
    val order: Int? = null,
    val voice: String? = null,
    val audioData: Map<String, Map<String, String>>? = null,
) {
    fun toDomain(): Page {
        return  Page(
            id = id,
//            pageRef = pageRef,
//            audio = audio,
            ocrText = ocrText,
            createAt = createAt?.toDate()?.time,
            createBy = createBy,
            updateAt = updateAt?.toDate()?.time,
            deleted = deleted,
            marked = marked,
            order = order,
            voice= voice,
            audioData=audioData
        )
    }
}