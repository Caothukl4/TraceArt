package com.tuananh.traceart.utils

import retrofit2.Response

class ApiException(
    val code: Int,
    override val message: String
) : Exception(message)

inline fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            try {
                Result.success(mapper(body))
            } catch (e: Exception) {
                Result.failure(e) // lỗi trong mapper
            }
        } else {
            Result.failure(ApiException(code(), "Response body is null"))
        }
    } else {
        Result.failure(ApiException(code(), errorBody()?.string() ?: "Unknown API error"))
    }
}