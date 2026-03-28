package com.aktech.ardrawing.data.remote.api

import com.aktech.ardrawing.data.remote.model.GenerateAudioRequest
import com.aktech.ardrawing.data.remote.model.GenerateContentRequest
import com.aktech.ardrawing.data.remote.model.GeneratedAudioDto
import com.aktech.ardrawing.data.remote.model.GeneratedContentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    suspend fun generateContent(
        @Body request: GenerateContentRequest,
        @Header("X-Firebase-AppCheck") appCheckToken: String,
        @Url url: String = "https://streamgeneratecontent-fmbfqk5iea-uc.a.run.app",
    ): Response<GeneratedContentDto>

    @POST
    suspend fun generateAudio(
        @Body request: GenerateAudioRequest,
        @Header("X-Firebase-AppCheck") appCheckToken: String,
        @Url url: String = "https://generateaudio-fmbfqk5iea-uc.a.run.app",
    ): Response<GeneratedAudioDto>
}