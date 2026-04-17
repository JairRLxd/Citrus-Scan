package com.example.citrusscan.data.repository

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.map
import com.example.citrusscan.data.remote.datasource.PredictionRemoteDataSource
import com.example.citrusscan.data.remote.mapper.toDomain
import com.example.citrusscan.domain.model.Prediction
import com.example.citrusscan.domain.model.PredictionInput
import com.example.citrusscan.domain.repository.PredictionRepository
import javax.inject.Inject

class PredictionRepositoryImpl @Inject constructor(
    private val remote: PredictionRemoteDataSource,
) : PredictionRepository {

    override suspend fun predict(input: PredictionInput): ApiResult<Prediction> =
        remote.predict(
            imageBytes = input.imageBytes,
            imageFileName = input.imageFileName,
            imageMimeType = input.imageMimeType,
            weight = input.weight,
            circumference = input.circumference,
        ).map { it.toDomain() }
}
