package com.example.citrusscan.domain.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.domain.model.PreprocessingRecommendation
import com.example.citrusscan.domain.model.PreprocessingRecommendationInput
import com.example.citrusscan.domain.model.PreprocessingStatus

interface PreprocessingRepository {
    suspend fun getStatus(): ApiResult<PreprocessingStatus>
    suspend fun getRecommendation(input: PreprocessingRecommendationInput): ApiResult<PreprocessingRecommendation>
}
