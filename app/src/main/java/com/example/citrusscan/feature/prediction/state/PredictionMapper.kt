package com.example.citrusscan.feature.prediction.state

import com.example.citrusscan.domain.model.ClassProbabilities
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ClassifierOutcome
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Label
import com.example.citrusscan.domain.model.Prediction
import java.util.Locale
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Prediction.toUi(): PredictionResultUiModel = PredictionResultUiModel(
    cards = results.map { it.toCard() },
    best = BestResultUiModel(
        bestClassifier = bestClassifier?.toDisplayName(),
        bestLabel = bestLabel?.toDisplayName(),
        hasBest = bestClassifier != null && bestLabel != null,
    ),
)

private fun ClassifierOutcome.toCard(): ClassifierCardUiModel = ClassifierCardUiModel(
    classifier = classifier.toDisplayName(),
    statusLabel = status.toStatusLabel(),
    statusKind = status.toStatusKind(),
    predictedLabel = predictedLabel?.toDisplayName(),
    confidencePercent = confidencePercent?.let { formatPercent(it) },
    confidenceRatio = confidence?.toFloat()?.coerceIn(0f, 1f),
    probabilities = probabilities.toProbabilityRows(),
    detail = detail,
)

private fun ClassProbabilities?.toProbabilityRows(): List<ProbabilityRow> = buildList {
    this@toProbabilityRows?.limon?.let {
        add(ProbabilityRow(label = "Limon", percent = formatPercent(it * 100.0), ratio = it.toFloat()))
    }
    this@toProbabilityRows?.naranja?.let {
        add(ProbabilityRow(label = "Naranja", percent = formatPercent(it * 100.0), ratio = it.toFloat()))
    }
}

private fun formatPercent(value: Double): String = String.format(Locale.US, "%.1f%%", value)

private fun ClassifierName.toDisplayName(): String = when (this) {
    ClassifierName.SVM -> "SVM"
    ClassifierName.BAYES -> "Bayes"
    ClassifierName.PERCEPTRON -> "Perceptron"
    ClassifierName.UNKNOWN -> "Desconocido"
}

private fun Label.toDisplayName(): String = when (this) {
    Label.LIMON -> "Limon"
    Label.NARANJA -> "Naranja"
    Label.UNKNOWN -> "Desconocido"
}

private fun ClassifierStatus.toStatusLabel(): String = when (this) {
    ClassifierStatus.OK -> "OK"
    ClassifierStatus.MODEL_NOT_TRAINED -> "Sin entrenar"
    ClassifierStatus.ERROR -> "Error"
    ClassifierStatus.UNKNOWN -> "Desconocido"
}

private fun ClassifierStatus.toStatusKind(): StatusKind = when (this) {
    ClassifierStatus.OK -> StatusKind.Success
    ClassifierStatus.MODEL_NOT_TRAINED -> StatusKind.Warning
    ClassifierStatus.ERROR -> StatusKind.Error
    ClassifierStatus.UNKNOWN -> StatusKind.Unknown
}

object PredictionResultCodec {
    private val json = Json { encodeDefaults = true }

    fun encode(model: PredictionResultUiModel): String =
        json.encodeToString(PredictionResultUiModel.serializer(), model)

    fun decode(payload: String): PredictionResultUiModel =
        json.decodeFromString(PredictionResultUiModel.serializer(), payload)
}
