package com.example.citrusscan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainRequestDto(
    @SerialName("dataset_path") val datasetPath: String? = null,
    val force: Boolean = false,
)

@Serializable
data class TrainResponseDto(
    val status: String,
    @SerialName("trained_classifiers") val trainedClassifiers: List<TrainedClassifierDto> = emptyList(),
    val detail: String? = null,
)

@Serializable
data class TrainedClassifierDto(
    val classifier: String,
    val status: String,
    val accuracy: Double? = null,
)
