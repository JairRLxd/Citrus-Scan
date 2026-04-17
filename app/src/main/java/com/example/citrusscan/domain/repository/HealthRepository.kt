package com.example.citrusscan.domain.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.domain.model.Health

interface HealthRepository {
    suspend fun getHealth(): ApiResult<Health>
}
