package com.tuananh.traceart.data.remote.datasource

import com.tuananh.traceart.data.remote.api.ApiService
import com.tuananh.traceart.data.remote.model.Config
import com.tuananh.traceart.data.remote.model.ContentItem
import com.tuananh.traceart.data.remote.model.ContentMessage
import com.tuananh.traceart.data.remote.model.GenerateAudioRequest
import com.tuananh.traceart.data.remote.model.GenerateContentRequest
import com.tuananh.traceart.data.remote.model.GeneratedAudioDto
import com.tuananh.traceart.data.remote.model.GeneratedContentDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun generateContent(
        base64Image: String,
        provider: String = "google",
        model: String = "gemini-2.0-flash-lite",
        appCheckToken: String,
    ): Response<GeneratedContentDto> = withContext(Dispatchers.IO) {
        val messages = listOf(
            ContentMessage(
                role = "user",
                content = listOf(
                    ContentItem(image = base64Image, type = "image"),
                    ContentItem(
                        text = "Convert the image to clean, readable text. Remove line breaks that occur mid-sentence, but keep paragraph breaks where appropriate. Reflow the text into natural paragraphs and remove page numbers.",
                        type = "text"
                    )
                )
            )
        )
        val request = GenerateContentRequest(
            provider = provider,
            model = model,
            messages = messages,
            config = Config(temperature = 0.0, topK = 1.0, topP = 0.1)
        )
        apiService.generateContent(request, appCheckToken)
    }

    suspend fun normalizeContent(
        text: String,
        provider: String = "google",
        model: String = "gemini-2.0-flash-lite",
        appCheckToken: String
    ): Response<GeneratedContentDto> = withContext(Dispatchers.IO) {

        val messages = listOf(
            ContentMessage(
                role = "user",
                content = listOf(
                    ContentItem(
                        text = "The input text is extracted from OCR and may contain errors.\n" +
                                "1. Correct all spelling and grammar.\n" +
                                "2. If the first line or last line consists only of numbers, remove them.\n" +
                                "3. Merge any line breaks that occur inside a sentence into a single space.\n" +
                                "4. Keep paragraph breaks only when they separate distinct ideas or logical sections.\n" +
                                "5. Do not change the meaning of the text.\n" +
                                "Only return the corrected text with proper paragraph formatting. Do not explain or add any comments.\n\n" +
                                "Text:" +
                                "\n$text",
                        type = "text"
                    )
                )
            )
        )
        val request = GenerateContentRequest(
            provider = provider,
            model = model,
            messages = messages,
            config = Config(temperature = 0.0, topK = 1.0, topP = 0.1)
        )
        apiService.generateContent(request, appCheckToken)
    }

    suspend fun generateAudio(
        input: String,
        bookId: String?,
        model: String? = "gpt-4o-mini-tts",
        voice: String? = "onyx",
        appCheckToken: String
    ): Response<GeneratedAudioDto> = withContext(Dispatchers.IO) {
        val request = GenerateAudioRequest(
            input = input,
            model = model,
            voice = voice,
            bookId = bookId,
        )
        apiService.generateAudio(request, appCheckToken)
    }
}