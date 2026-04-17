package com.example.citrusscan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredictResponseDto(
    val results: List<ClassifierResultDto> = emptyList(),
    @SerialName("best_classifier") val bestClassifier: String? = null,
    @SerialName("best_label") val bestLabel: String? = null,
)

@Serializable
data class ClassifierResultDto(
    val classifier: String,
    val status: String,
    @SerialName("predicted_label") val predictedLabel: String? = null,
    val confidence: Double? = null,
    @SerialName("confidence_percent") val confidencePercent: Double? = null,
    @SerialName("class_probabilities") val classProbabilities: ClassProbabilitiesDto? = null,
    val detail: String? = null,
)

@Serializable
data class ClassProbabilitiesDto(
    val limon: Double? = null,
    val naranja: Double? = null,
)
