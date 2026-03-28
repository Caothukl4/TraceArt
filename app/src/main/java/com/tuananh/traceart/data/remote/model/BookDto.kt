package com.tuananh.traceart.data.remote.model

import com.tuananh.traceart.domain.model.Book
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class BookDto(
    var id: String = "",
    val name: String? = null,
    val imgCover: String? = null,
    val createAt: Timestamp? = null,
    val createBy: String? = null,
    val updateAt: Timestamp? = null,
    val totalPages: Int? = null,
    val currentPageId: String? = null,
    val author: String? = null,
    var deleted: Boolean? = false,
    var marked: Boolean? = false,
    val order: Int? = null,
    val uids: List<String>? = null,
    val coverUrl: String? = null
) {
    fun toDomain(): Book {
        return Book(
            id = id,
            name = name,
            createAt = createAt?.toDate()?.time,
            createBy = createBy,
            updateAt = updateAt?.toDate()?.time,
            totalPages = totalPages,
            currentPageId = currentPageId,
            author = author,
            deleted = deleted,
            marked = marked,
            order = order,
            uids = uids,
            coverUrl = coverUrl
        )
    }
}