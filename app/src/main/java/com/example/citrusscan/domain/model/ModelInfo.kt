package com.example.citrusscan.domain.model

data class ModelInfo(
    val classifier: ClassifierName,
    val trained: Boolean,
    val lastTrainedAt: String?,
    val accuracy: Double?,
)
