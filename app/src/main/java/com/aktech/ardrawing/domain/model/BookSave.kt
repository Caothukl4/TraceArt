package com.aktech.ardrawing.domain.model

data class BookSave(
    val unitedNumber: Int? = null,
    val localBookData: LocalBookData? = null
)

data class LocalPageData(
//    val imgOriginalUri: String? = null,
    val imgCroppedUri: String? = null,
    val audio: String? = null
)

typealias LocalBookData = Map<String, Map<String, LocalPageData>>

//data class Generating(
//    val isGeneratingText: Boolean = false,
//    val isGeneratedAudio: Boolean = false
//)

enum class GeneratingType {
    GeneratingText,
    GeneratingAudio
}

typealias GeneratingBookData = Map<String, GeneratingType?>