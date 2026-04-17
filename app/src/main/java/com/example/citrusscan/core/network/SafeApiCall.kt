package com.example.citrusscan.core.network

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.AppError
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

suspend inline fun <T> safeApiCall(
    errorParser: ApiErrorParser? = null,
    crossinline block: suspend () -> T,
): ApiResult<T> = try {
    ApiResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: SocketTimeoutException) {
    ApiResult.Failure(AppError.Timeout(e.message))
} catch (e: HttpException) {
    val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
    val serverMessage = errorParser?.extractMessage(body) ?: body
    ApiResult.Failure(
        AppError.Http(
            code = e.code(),
            serverMessage = serverMessage,
            message = e.message(),
        )
    )
} catch (e: IOException) {
    ApiResult.Failure(AppError.Network(e.message))
} catch (e: SerializationException) {
    ApiResult.Failure(AppError.Serialization(e.message))
} catch (e: Throwable) {
    ApiResult.Failure(AppError.Unknown(e.message, e))
}
