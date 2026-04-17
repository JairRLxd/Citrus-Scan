package com.example.citrusscan.data.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.map
import com.example.citrusscan.data.remote.datasource.HealthRemoteDataSource
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.domain.model.Health
import com.example.citrusscan.domain.repository.HealthRepository
import javax.inject.Inject

class HealthRepositoryImpl @Inject constructor(
    private val remote: HealthRemoteDataSource,
) : HealthRepository {
    override suspend fun getHealth(): ApiResult<Health> =
        remote.getHealth().map { it.toDomain() }
}
