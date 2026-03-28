package com.tuananh.traceart.domain.repository

interface FirebaseFunctionsRepository {
    suspend fun generateAudio(
        input: String,
        bookId: String?,
        voice: String?,
        provider: String?,
    ): String


}