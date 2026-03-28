package com.aktech.ardrawing.data.repository

import com.aktech.ardrawing.data.remote.datasource.ApiRemoteDataSource
//import com.bookecho.scan2hear.domain.api.UiState
import com.aktech.ardrawing.domain.model.GeneratedAudio
import com.aktech.ardrawing.domain.model.GeneratedContent
import com.aktech.ardrawing.domain.repository.ApiRepository
import com.aktech.ardrawing.domain.repository.CommonFirebaseRepository
import com.aktech.ardrawing.utils.toResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepositoryImpl @Inject constructor(
    private val apiRemoteDataSource: ApiRemoteDataSource,
    private val commonFirebaseRepository: CommonFirebaseRepository,
) : ApiRepository {

    override suspend fun generateContent(
        base64Image: String
    ): Result<GeneratedContent> {
        try {
            val appCheckToken = commonFirebaseRepository.getAppCheckToken()
            return if (appCheckToken != null) {
                apiRemoteDataSource.generateContent(base64Image, appCheckToken = appCheckToken)
                    .toResult { it.toDomain() }
            } else {
                Result.failure(Exception("Not found appCheckToken"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun normalizeContent(text: String): Result<GeneratedContent> {
        try {
            val appCheckToken = commonFirebaseRepository.getAppCheckToken()
            return if (appCheckToken != null) {
                apiRemoteDataSource.normalizeContent(text, appCheckToken = appCheckToken)
                    .toResult { it.toDomain() }
            } else {
                Result.failure(Exception("Not found appCheckToken"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun generateAudio(
        input: String,
        bookId: String?,
        voice: String?,
    ): Result<GeneratedAudio> {
        try {
            val appCheckToken = commonFirebaseRepository.getAppCheckToken()
            return if (appCheckToken != null) {
                apiRemoteDataSource.generateAudio(input, bookId, appCheckToken = appCheckToken, voice = voice)
                    .toResult { it.toDomain() }
            } else {
                Result.failure(Exception("Not found appCheckToken"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}