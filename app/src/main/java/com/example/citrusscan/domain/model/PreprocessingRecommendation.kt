package com.example.citrusscan.domain.model

data class PreprocessingRecommendation(
    val pipeline: List<String>,
    val notes: String?,
)

data class PreprocessingRecommendationInput(
    val weight: Float,
    val circumference: Float,
)
