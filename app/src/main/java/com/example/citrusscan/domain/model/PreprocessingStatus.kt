package com.example.citrusscan.domain.model

data class PreprocessingStatus(
    val ready: Boolean,
    val pipeline: List<String>,
    val detail: String?,
)
