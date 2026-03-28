package com.tuananh.traceart.domain.params

import android.os.Parcelable
import com.tuananh.traceart.data.remote.model.BookDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookParams(
    val name: String? = null,
    val imgCover: String? = null,
    val createBy: String? = null,
    val totalPages: Int? = null,
    val currentPageId: String? = null,
    val author: String? = null,
    val deleted: Boolean? = null,
    val marked: Boolean? = null,
    val order: Int? = null,
    val uids: List<String>? = null,
    val coverUrl: String? = null,
    val anonymousId: String? = null
) : Parcelable {
    fun toBookDto(id: String): BookDto {
        return BookDto(
            id = id,
            name = name,
            imgCover = imgCover,
            createBy = createBy,
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