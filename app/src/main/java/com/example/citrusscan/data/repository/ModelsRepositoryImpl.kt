package com.example.citrusscan.data.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.map
import com.example.citrusscan.data.remote.datasource.ModelsRemoteDataSource
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.domain.model.ModelInfo
import com.example.citrusscan.domain.repository.ModelsRepository
import javax.inject.Inject

class ModelsRepositoryImpl @Inject constructor(
    private val remote: ModelsRemoteDataSource,
) : ModelsRepository {
    override suspend fun getModels(): ApiResult<List<ModelInfo>> =
        remote.getModels().map { it.toDomain() }
}
