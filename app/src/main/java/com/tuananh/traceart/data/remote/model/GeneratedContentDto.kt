package com.tuananh.traceart.data.remote.model

import com.tuananh.traceart.domain.model.GeneratedContent

data class GeneratedContentDto (
    val text: String,
){
    fun toDomain() = GeneratedContent(text)
}