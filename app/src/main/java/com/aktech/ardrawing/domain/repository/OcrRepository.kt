package com.aktech.ardrawing.domain.repository

interface OcrRepository {
    suspend fun scanImageFromPath(imagePath: String): String?
}