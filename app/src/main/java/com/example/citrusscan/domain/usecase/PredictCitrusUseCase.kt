package com.example.citrusscan.domain.usecase

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.validation.Validators
import com.example.citrusscan.domain.model.ClassifierOutcome
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Prediction
import com.example.citrusscan.domain.model.PredictionInput
import com.example.citrusscan.domain.repository.PredictionRepository
import javax.inject.Inject

class PredictCitrusUseCase @Inject constructor(
    private val repository: PredictionRepository,
    private val validators: Validators,
) {
    suspend operator fun invoke(input: PredictionInput): ApiResult<Prediction> {
        validators.validateImage(input.imageBytes)?.let { return ApiResult.Failure(it) }
        validators.validateWeight(input.weight)?.let { return ApiResult.Failure(it) }
        validators.validateCircumference(input.circumference)?.let { return ApiResult.Failure(it) }

        return when (val result = repository.predict(input)) {
            is ApiResult.Success -> ApiResult.Success(result.data.sortedByConfidence())
            is ApiResult.Failure -> result
        }
    }

    private fun Prediction.sortedByConfidence(): Prediction = copy(
        results = results.sortedWith(
            compareByDescending<ClassifierOutcome> { it.status == ClassifierStatus.OK }
                .thenByDescending { it.confidencePercent ?: -1.0 }
        )
    )
}
