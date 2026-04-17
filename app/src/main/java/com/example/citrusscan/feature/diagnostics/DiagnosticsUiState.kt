package com.example.citrusscan.feature.diagnostics

import com.example.citrusscan.domain.model.Health
import com.example.citrusscan.domain.model.ModelInfo
import com.example.citrusscan.domain.model.PreprocessingStatus

data class DiagnosticsUiState(
    val isLoading: Boolean = true,
    val health: Health? = null,
    val models: List<ModelInfo> = emptyList(),
    val preprocessingStatus: PreprocessingStatus? = null,
    val healthError: String? = null,
    val modelsError: String? = null,
    val preprocessingError: String? = null,
)
