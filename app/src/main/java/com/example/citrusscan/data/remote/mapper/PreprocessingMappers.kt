package com.example.citrusscan.data.remote.mapper

import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationDto
import com.example.citrusscan.data.remote.dto.PreprocessingRecommendationRequestDto
import com.example.citrusscan.data.remote.dto.PreprocessingStatusDto
import com.example.citrusscan.domain.model.PreprocessingRecommendation
import com.example.citrusscan.domain.model.PreprocessingRecommendationInput
import com.example.citrusscan.domain.model.PreprocessingStatus

fun PreprocessingStatusDto.toDomain(): PreprocessingStatus = PreprocessingStatus(
    ready = ready,
    pipeline = pipeline,
    detail = detail,
)

fun PreprocessingRecommendationDto.toDomain(): PreprocessingRecommendation =
    PreprocessingRecommendation(
        pipeline = recommendedPipeline,
        notes = notes,
    )

fun PreprocessingRecommendationInput.toDto(): PreprocessingRecommendationRequestDto =
    PreprocessingRecommendationRequestDto(
        weight = weight,
        circumference = circumference,
    )
