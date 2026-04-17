package com.example.citrusscan.data.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.map
import com.example.citrusscan.data.remote.datasource.TrainingRemoteDataSource
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.data.remote.mapper.toDto
import com.example.citrusscan.domain.model.TrainingInput
import com.example.citrusscan.domain.model.TrainingReport
import com.example.citrusscan.domain.repository.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val remote: TrainingRemoteDataSource,
) : TrainingRepository {
    override suspend fun train(input: TrainingInput): ApiResult<TrainingReport> =
        remote.train(input.toDto()).map { it.toDomain() }
}
