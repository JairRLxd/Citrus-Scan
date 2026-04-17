package com.example.citrusscan.data.remote.mapper

import com.example.citrusscan.data.remote.dto.TrainRequestDto
import com.example.citrusscan.data.remote.dto.TrainResponseDto
import com.example.citrusscan.data.remote.dto.TrainedClassifierDto
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.TrainedClassifier
import com.example.citrusscan.domain.model.TrainingInput
import com.example.citrusscan.domain.model.TrainingReport
import com.example.citrusscan.domain.model.TrainingStatus

fun TrainingInput.toDto(): TrainRequestDto = TrainRequestDto(
    datasetPath = datasetPath,
    force = force,
)

fun TrainResponseDto.toDomain(): TrainingReport = TrainingReport(
    success = status.equals("ok", ignoreCase = true),
    items = trainedClassifiers.map { it.toDomain() },
    detail = detail,
)

fun TrainedClassifierDto.toDomain(): TrainedClassifier = TrainedClassifier(
    classifier = ClassifierName.fromApi(classifier),
    accuracy = accuracy,
    status = TrainingStatus.fromApi(status),
)
