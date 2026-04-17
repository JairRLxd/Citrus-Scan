package com.example.citrusscan.feature.prediction.ui.capture

import androidx.lifecycle.ViewModel
import com.example.citrusscan.feature.prediction.state.CaptureUiEvent
import com.example.citrusscan.feature.prediction.state.CaptureUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CaptureViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CaptureUiState())
    val state: StateFlow<CaptureUiState> = _state.asStateFlow()

    fun onEvent(event: CaptureUiEvent) {
        when (event) {
            is CaptureUiEvent.ImageSelected -> {
                _state.update { it.copy(selectedImageUri = event.uri) }
            }

            CaptureUiEvent.ClearSelection -> {
                _state.value = CaptureUiState()
            }
        }
    }
}
