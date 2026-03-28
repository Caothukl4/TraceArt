package com.tuananh.traceart.data.repository

import android.content.Context
import com.tuananh.traceart.data.manager.TextRecognizerManager
import com.tuananh.traceart.domain.repository.OcrRepository
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