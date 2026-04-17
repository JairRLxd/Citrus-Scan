package com.example.citrusscan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelsDto(
    val models: List<ModelItemDto> = emptyList(),
)

@Serializable
data class ModelItemDto(
    val classifier: String,
    val trained: Boolean,
    @SerialName("last_trained_at") val lastTrainedAt: String? = null,
    val accuracy: Double? = null,
)
