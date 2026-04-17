package com.example.citrusscan.domain.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.domain.model.ModelInfo

interface ModelsRepository {
    suspend fun getModels(): ApiResult<List<ModelInfo>>
}
