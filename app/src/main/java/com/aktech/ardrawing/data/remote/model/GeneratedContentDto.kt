package com.aktech.ardrawing.data.remote.model

import com.aktech.ardrawing.domain.model.GeneratedContent

data class GeneratedContentDto (
    val text: String,
){
    fun toDomain() = GeneratedContent(text)
}