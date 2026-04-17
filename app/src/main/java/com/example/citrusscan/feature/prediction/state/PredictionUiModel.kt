package com.example.citrusscan.feature.prediction.state

import kotlinx.serialization.Serializable

@Serializable
enum class StatusKind {
    Success,
    Warning,
    Error,
    Unknown,
}

@Serializable
data class ProbabilityRow(
    val label: String,
    val percent: String,
    val ratio: Float,
)

@Serializable
data class ClassifierCardUiModel(
    val classifier: String,
    val statusLabel: String,
    val statusKind: StatusKind,
    val predictedLabel: String?,
    val confidencePercent: String?,
    val confidenceRatio: Float?,
    val probabilities: List<ProbabilityRow>,
    val detail: String?,
)

@Serializable
data class BestResultUiModel(
    val bestClassifier: String?,
    val bestLabel: String?,
    val hasBest: Boolean,
)

@Serializable
data class PredictionResultUiModel(
    val cards: List<ClassifierCardUiModel>,
    val best: BestResultUiModel,
)

sealed interface PredictionFormEffect {
    data class NavigateToResult(val payload: String) : PredictionFormEffect
}
