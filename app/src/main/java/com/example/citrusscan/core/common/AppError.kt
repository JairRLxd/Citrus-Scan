package com.example.citrusscan.core.common

sealed class AppError(open val message: String?) {
    data class Network(override val message: String? = null) : AppError(message)
    data class Timeout(override val message: String? = null) : AppError(message)
    data class Http(
        val code: Int,
        val serverMessage: String? = null,
        override val message: String? = null,
    ) : AppError(message)
    data class Serialization(override val message: String?) : AppError(message)
    data class Validation(val field: String, override val message: String) : AppError(message)
    data class Unknown(
        override val message: String?,
        val cause: Throwable? = null,
    ) : AppError(message)
}
