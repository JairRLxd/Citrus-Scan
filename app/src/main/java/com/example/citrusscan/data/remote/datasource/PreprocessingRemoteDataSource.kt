package com.example.citrusscan.data.remote.datasource

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.IoDispatcher
import com.example.citrusscan.core.network.ApiErrorParser
import com.example.citrusscan.core.network.safeApiCall
import com.example.citrusscan.data.remote.api.CitrusScanApi
import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationDto
import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationRequestDto
import com.example.citrusscan.data.remote.dto.PreprocessingStatusDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PreprocessingRemoteDataSource @Inject constructor(
    private val api: CitrusScanApi,
    private val errorParser: ApiErrorParser,
    @IoDispatcher private val io: CoroutineDispatcher,
) {
    suspend fun getStatus(): ApiResult<PreprocessingStatusDto> = withContext(io) {
        safeApiCall(errorParser) { api.preprocessingStatus() }
    }

    suspend fun getRecommendation(
        body: PreprocessingRecommendationRequestDto,
    ): ApiResult<PreprocessingRecommendationDto> = withContext(io) {
        safeApiCall(errorParser) { api.preprocessingRecommendation(body) }
    }
}
