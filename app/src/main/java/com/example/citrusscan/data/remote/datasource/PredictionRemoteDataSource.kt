package com.example.citrusscan.data.remote.datasource

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.IoDispatcher
import com.example.citrusscan.core.network.ApiErrorParser
import com.example.citrusscan.core.network.safeApiCall
import com.example.citrusscan.data.remote.api.CitrusScanApi
import com.example.citrusscan.data.remote.dto.PredictResponseDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PredictionRemoteDataSource @Inject constructor(
    private val api: CitrusScanApi,
    private val errorParser: ApiErrorParser,
    @IoDispatcher private val io: CoroutineDispatcher,
) {
    suspend fun predict(
        imageBytes: ByteArray,
        imageFileName: String,
        imageMimeType: String,
        weight: Float,
        circumference: Float,
    ): ApiResult<PredictResponseDto> = withContext(io) {
        safeApiCall(errorParser) {
            val filePart = MultipartBody.Part.createFormData(
                name = "file",
                filename = imageFileName,
                body = imageBytes.toRequestBody(imageMimeType.toMediaType()),
            )
            val textPlain = "text/plain".toMediaType()
            val weightPart = weight.toString().toRequestBody(textPlain)
            val circumPart = circumference.toString().toRequestBody(textPlain)
            api.predict(filePart, weightPart, circumPart)
        }
    }
}
