package com.tuananh.traceart.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String = "",
    val name: String? = null,
    val createAt: Long? = null,
    val createBy: String? = null,
    val updateAt: Long? = null,
    val totalPages: Int? = null,
    val currentPageId: String? = null,
    val author: String? = null,
    val deleted: Boolean? = null,
    val marked: Boolean? = null,
    val order: Int? = null,
    val uids: List<String>? = null,
    val coverUrl: String? = null
): Parcelable {
    fun mergeWith(new: Book): Book {
        return this.copy(
            name = new.name ?: this.name,
            createAt = new.createAt ?: this.createAt,
            createBy = new.createBy ?: this.createBy,
            updateAt = new.updateAt ?: this.updateAt,
            totalPages = new.totalPages ?: this.totalPages,
            currentPageId = new.currentPageId ?: this.currentPageId,
            author = new.author ?: this.author,
            deleted = new.deleted ?: this.deleted,
            marked = new.marked ?: this.marked,
            order = new.order ?: this.order,
            uids = new.uids ?: this.uids,
            coverUrl = new.coverUrl ?: this.coverUrl
        )
    }
}