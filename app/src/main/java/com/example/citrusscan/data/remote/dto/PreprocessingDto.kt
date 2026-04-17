package com.example.citrusscan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreprocessingStatusDto(
    val ready: Boolean,
    val pipeline: List<String> = emptyList(),
    val detail: String? = null,
)

@Serializable
data class PreprocessingRecommendationRequestDto(
    val weight: Float,
    val circumference: Float,
)

@Serializable
data class PreprocessingRecommendationDto(
    @SerialName("recommended_pipeline") val recommendedPipeline: List<String> = emptyList(),
    val notes: String? = null,
)
