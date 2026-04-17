package com.example.citrusscan.domain.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.domain.model.Prediction
import com.example.citrusscan.domain.model.PredictionInput

interface PredictionRepository {
    suspend fun predict(input: PredictionInput): ApiResult<Prediction>
}
