package com.aktech.ardrawing.domain.model

import java.util.Locale

data class Language(
    val imgResource: Int = 0,
    val name: String = "",
    val languageTag: String = ""
) {
    fun toLocale(): Locale{
        return Locale.forLanguageTag(languageTag)
    }
}