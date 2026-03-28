package com.tuananh.traceart.domain.repository

import com.tuananh.traceart.domain.model.Book
import com.tuananh.traceart.domain.model.LimitData
import com.tuananh.traceart.domain.params.BookParams
import com.tuananh.traceart.domain.model.Page
import com.tuananh.traceart.domain.model.Sample
import com.tuananh.traceart.domain.model.VoiceInfo
import com.tuananh.traceart.domain.params.LimitParams
import com.tuananh.traceart.domain.params.PageParams
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun fetchListBook(): List<Book>

    suspend fun fetchListBookByAnonymousId(anonymousId: String, marked: Boolean = false): List<Book>

    suspend fun fetchListBookByUserId(userId: String): List<Book>


    suspend fun getBookById(bookId: String): Book?

    suspend fun fetchListPage(bookId: String): List<Page>

    fun listenListPage(bookId: String): Flow<List<Page>>

    suspend fun createBook(params: BookParams): Book

    suspend fun updateBook(bookId: String, params: BookParams)

    suspend fun addPage(bookId: String, params: PageParams): Page

    suspend fun updatePage(bookId: String, pageId: String, params: PageParams)

    suspend fun deleteBook(bookId: String)

    suspend fun deletePage(bookId: String, pageId: String)

    suspend fun getListVoices(languageTag: String): List<VoiceInfo>

    suspend fun getMapSamples(): Map<String, Sample>

    suspend fun updatePageAudioData(bookId: String, pageId: String, sha1Key: String, voice: String, audio: String)

    suspend fun getLimitData(deviceId: String): LimitData?

    suspend fun updateLimitData(deviceId: String, limitParams: LimitParams)
}