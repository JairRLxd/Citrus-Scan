package com.example.citrusscan.domain.usecase

import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.validation.Validators
import com.example.citrusscan.domain.model.PreprocessingRecommendation
import com.example.citrusscan.domain.model.PreprocessingRecommendationInput
import com.example.citrusscan.domain.repository.PreprocessingRepository
import javax.inject.Inject

class GetPreprocessingRecommendationUseCase @Inject constructor(
    private val repository: PreprocessingRepository,
    private val validators: Validators,
) {
    suspend operator fun invoke(
        input: PreprocessingRecommendationInput,
    ): ApiResult<PreprocessingRecommendation> {
        validators.validateWeight(input.weight)?.let { return ApiResult.Failure(it) }
        validators.validateCircumference(input.circumference)?.let { return ApiResult.Failure(it) }
        return repository.getRecommendation(input)
    }
}
