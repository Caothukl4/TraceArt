package com.aktech.ardrawing.domain.repository

import com.aktech.ardrawing.domain.model.GeneratedAudio
import com.aktech.ardrawing.domain.model.GeneratedContent

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