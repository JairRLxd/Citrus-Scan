package com.example.citrusscan.feature.prediction.ui.result

import androidx.lifecycle.ViewModel
import com.example.citrusscan.feature.prediction.state.PredictionResultCodec
import com.example.citrusscan.feature.prediction.state.PredictionResultUiEvent
import com.example.citrusscan.feature.prediction.state.PredictionResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class PredictionResultViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<PredictionResultUiState>(PredictionResultUiState.Idle)
    val state: StateFlow<PredictionResultUiState> = _state.asStateFlow()

    private var loadedPayload: String? = null

    fun load(payload: String?) {
        if (payload == loadedPayload && _state.value !is PredictionResultUiState.Idle) return
        loadedPayload = payload
        _state.value = PredictionResultUiState.Loading
        _state.value = runCatching {
            val result = requireNotNull(payload) { "No hay un resultado disponible todavia." }
            PredictionResultUiState.Success(PredictionResultCodec.decode(result))
        }.getOrElse {
            PredictionResultUiState.Error(it.message ?: "No se pudo abrir el resultado.")
        }
    }

    fun onEvent(event: PredictionResultUiEvent) {
        when (event) {
            PredictionResultUiEvent.Clear -> {
                loadedPayload = null
                _state.value = PredictionResultUiState.Idle
            }
        }
    }
}
