package com.example.citrusscan.core.network

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Singleton
class ApiErrorParser @Inject constructor(
    private val json: Json,
) {
    /**
     * The backend returns errors as `{"detail": "..."}`. We try to extract it and
     * fall back to the raw body if parsing fails.
     */
    fun extractMessage(rawBody: String?): String? {
        if (rawBody.isNullOrBlank()) return null
        return runCatching {
            json.decodeFromString<ErrorBody>(rawBody).detail
        }.getOrElse { rawBody }
    }
}

@Serializable
private data class ErrorBody(val detail: String? = null)
