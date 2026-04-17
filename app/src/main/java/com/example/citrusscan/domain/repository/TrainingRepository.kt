package com.example.citrusscan.domain.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.domain.model.TrainingInput
import com.example.citrusscan.domain.model.TrainingReport

interface TrainingRepository {
    suspend fun train(input: TrainingInput): ApiResult<TrainingReport>
}
