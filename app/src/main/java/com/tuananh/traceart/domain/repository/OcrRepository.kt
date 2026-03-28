package com.tuananh.traceart.domain.repository

interface OcrRepository {
    suspend fun scanImageFromPath(imagePath: String): String?
}