package com.tuananh.traceart.domain.repository

import com.tuananh.traceart.domain.model.GeneratedAudio
import com.tuananh.traceart.domain.model.GeneratedContent

interface ApiRepository {
    suspend fun generateContent(
        base64Image: String
    ): Result<GeneratedContent>

    suspend fun generateAudio(
        input: String,
        bookId: String?,
        voice: String?
    ): Result<GeneratedAudio>

    suspend fun normalizeContent(text: String): Result<GeneratedContent>
}