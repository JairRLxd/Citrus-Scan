package com.example.citrusscan.feature.prediction.state

data class CaptureUiState(
    val selectedImageUri: String? = null,
) {
    val canContinue: Boolean = !selectedImageUri.isNullOrBlank()
}

data class PredictionFormUiState(
    val selectedImageUri: String? = null,
    val weight: String = "",
    val weightError: String? = null,
    val circumference: String = "",
    val circumferenceError: String? = null,
    val imageError: String? = null,
    val submitError: String? = null,
    val isSubmitting: Boolean = false,
) {
    val isValid: Boolean =
        !selectedImageUri.isNullOrBlank() &&
            weight.toFloatOrNull()?.let { it > 0f } == true &&
            circumference.toFloatOrNull()?.let { it > 0f } == true &&
            !isSubmitting
}

sealed interface PredictionResultUiState {
    data object Idle : PredictionResultUiState
    data object Loading : PredictionResultUiState
    data class Success(val data: PredictionResultUiModel) : PredictionResultUiState
    data class Error(val message: String) : PredictionResultUiState
}
