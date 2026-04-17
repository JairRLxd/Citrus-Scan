package com.example.citrusscan.feature.prediction.state

sealed interface CaptureUiEvent {
    data class ImageSelected(val uri: String) : CaptureUiEvent
    data object ClearSelection : CaptureUiEvent
}

sealed interface PredictionUiEvent {
    data class ImagePicked(val uri: String) : PredictionUiEvent
    data class WeightChanged(val value: String) : PredictionUiEvent
    data class CircumferenceChanged(val value: String) : PredictionUiEvent
    data object Submit : PredictionUiEvent
    data object Retry : PredictionUiEvent
    data object Reset : PredictionUiEvent
}

sealed interface PredictionResultUiEvent {
    data object Clear : PredictionResultUiEvent
}
