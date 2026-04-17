package com.example.citrusscan.data.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.map
import com.example.citrusscan.data.remote.datasource.PreprocessingRemoteDataSource
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.data.remote.mapper.toDto
import com.example.citrusscan.domain.model.PreprocessingRecommendation
import com.example.citrusscan.domain.model.PreprocessingRecommendationInput
import com.example.citrusscan.domain.model.PreprocessingStatus
import com.example.citrusscan.domain.repository.PreprocessingRepository
import javax.inject.Inject

class PreprocessingRepositoryImpl @Inject constructor(
    private val remote: PreprocessingRemoteDataSource,
) : PreprocessingRepository {

    override suspend fun getStatus(): ApiResult<PreprocessingStatus> =
        remote.getStatus().map { it.toDomain() }

    override suspend fun getRecommendation(
        input: PreprocessingRecommendationInput,
    ): ApiResult<PreprocessingRecommendation> =
        remote.getRecommendation(input.toDto()).map { it.toDomain() }
}
