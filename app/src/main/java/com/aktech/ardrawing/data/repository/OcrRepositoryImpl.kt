package com.aktech.ardrawing.data.repository

import android.content.Context
import com.aktech.ardrawing.data.manager.TextRecognizerManager
import com.aktech.ardrawing.domain.repository.OcrRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OcrRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val textRecognizerManager: TextRecognizerManager,
) : OcrRepository {
    override suspend fun scanImageFromPath(imagePath: String): String? {
        return textRecognizerManager.scanImageFromPath(imagePath)
    }

}