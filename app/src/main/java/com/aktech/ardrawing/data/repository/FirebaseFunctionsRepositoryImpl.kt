package com.aktech.ardrawing.data.repository

import com.aktech.ardrawing.domain.repository.FirebaseFunctionsRepository
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFunctionsRepositoryImpl @Inject constructor() : FirebaseFunctionsRepository {
    val functions: FirebaseFunctions = FirebaseFunctions.getInstance()
    override suspend fun generateAudio(
        input: String,
        bookId: String?,
        voice: String?,
        provider: String?,
    ): String {
        val data = hashMapOf(
            "input" to input,
            "voice" to (voice ?: "onyx"),
            "model" to "gpt-4o-mini-tts",
            "bookId" to bookId,
            "provider" to provider,
        )

        val result = functions
            .getHttpsCallable("generateAudioV2")
            .call(data)
            .await()

        val resultData = result.data as Map<*, *>
        return resultData["url"] as String
    }


}