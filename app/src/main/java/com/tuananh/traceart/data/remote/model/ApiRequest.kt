package com.tuananh.traceart.data.remote.model

data class GenerateContentRequest(
    val provider: String,
    val model: String,
    val messages: List<ContentMessage>,
    val config: Config
)

data class Config(
    val temperature: Double,
    val topK: Double,
    val topP: Double,
)

data class GenerateAudioRequest(
    val input: String,
    val model: String?,
    val voice: String?,
    val bookId: String?
)

data class ContentMessage(
    val role: String,
    val content: List<ContentItem>
)

data class ContentItem(
    val image: String? = null,
    val text: String? = null,
    val type: String
)