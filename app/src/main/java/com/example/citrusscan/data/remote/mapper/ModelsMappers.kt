package com.example.citrusscan.data.remote.mapper

import com.example.citrusscan.data.remote.dto.ModelItemDto
import com.example.citrusscan.data.remote.dto.ModelsDto
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.domain.model.ModelInfo

fun ModelsDto.toDomain(): List<ModelInfo> = models.map { it.toDomain() }

fun ModelItemDto.toDomain(): ModelInfo = ModelInfo(
    classifier = ClassifierName.fromApi(classifier),
    trained = trained,
    lastTrainedAt = lastTrainedAt,
    accuracy = accuracy,
)
