package com.example.citrusscan.data.remote.mapper

import com.example.citrusscan.data.remote.dto.ClassProbabilitiesDto
import com.example.citrusscan.data.remote.dto.ClassifierResultDto
import com.example.citrusscan.data.remote.dto.PredictResponseDto
import com.example.citrusscan.domain.model.ClassProbabilities
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ClassifierOutcome
import com.example.citrusscan.domain.model.ClassifierStatus
import com.example.citrusscan.domain.model.Label
import com.example.citrusscan.domain.model.Prediction

fun PredictResponseDto.toDomain(): Prediction = Prediction(
    results = results.map { it.toDomain() },
    bestClassifier = bestClassifier?.let { ClassifierName.fromApi(it) }
        ?.takeIf { it != ClassifierName.UNKNOWN },
    bestLabel = Label.fromApi(bestLabel),
)

fun ClassifierResultDto.toDomain(): ClassifierOutcome = ClassifierOutcome(
    classifier = ClassifierName.fromApi(classifier),
    status = ClassifierStatus.fromApi(status),
    predictedLabel = Label.fromApi(predictedLabel),
    confidence = confidence,
    confidencePercent = confidencePercent ?: confidence?.let { it * 100.0 },
    probabilities = classProbabilities?.toDomain(),
    detail = detail,
)

fun ClassProbabilitiesDto.toDomain(): ClassProbabilities = ClassProbabilities(
    limon = limon,
    naranja = naranja,
)
