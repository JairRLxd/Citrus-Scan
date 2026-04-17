package com.example.citrusscan.feature.prediction.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.ErrorMessenger
import com.example.citrusscan.domain.model.PredictionInput
import com.example.citrusscan.domain.usecase.PredictCitrusUseCase
import com.example.citrusscan.feature.prediction.image.PredictionImageReader
import com.example.citrusscan.feature.prediction.state.PredictionFormEffect
import com.example.citrusscan.feature.prediction.state.PredictionFormUiState
import com.example.citrusscan.feature.prediction.state.PredictionResultCodec
import com.example.citrusscan.feature.prediction.state.PredictionUiEvent
import com.example.citrusscan.feature.prediction.state.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PredictionFormViewModel @Inject constructor(
    private val predictCitrusUseCase: PredictCitrusUseCase,
    private val imageReader: PredictionImageReader,
    private val errorMessenger: ErrorMessenger,
) : ViewModel() {

    private val _state = MutableStateFlow(PredictionFormUiState())
    val state: StateFlow<PredictionFormUiState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<PredictionFormEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<PredictionFormEffect> = _effects.asSharedFlow()

    fun onEvent(event: PredictionUiEvent) {
        when (event) {
            is PredictionUiEvent.ImagePicked -> {
                _state.update {
                    it.copy(
                        selectedImageUri = event.uri,
                        imageError = null,
                        submitError = null,
                    )
                }
            }

            is PredictionUiEvent.WeightChanged -> {
                _state.update {
                    it.copy(
                        weight = event.value,
                        weightError = null,
                        submitError = null,
                    )
                }
            }

            is PredictionUiEvent.CircumferenceChanged -> {
                _state.update {
                    it.copy(
                        circumference = event.value,
                        circumferenceError = null,
                        submitError = null,
                    )
                }
            }

            PredictionUiEvent.Submit,
            PredictionUiEvent.Retry -> submit()

            PredictionUiEvent.Reset -> {
                _state.value = PredictionFormUiState()
            }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            val snapshot = _state.value
            val imageUri = snapshot.selectedImageUri
            val weight = snapshot.weight.toFloatOrNull()
            val circumference = snapshot.circumference.toFloatOrNull()

            var hasError = false
            _state.update {
                val nextState = it.copy(
                    imageError = if (imageUri.isNullOrBlank()) "Selecciona una imagen." else null,
                    weightError = if (weight == null) "Peso invalido." else null,
                    circumferenceError = if (circumference == null) "Circunferencia invalida." else null,
                    submitError = null,
                )
                hasError =
                    nextState.imageError != null ||
                        nextState.weightError != null ||
                        nextState.circumferenceError != null
                nextState
            }
            if (hasError || imageUri == null || weight == null || circumference == null) return@launch

            _state.update { it.copy(isSubmitting = true) }

            when (val imageResult = imageReader.read(imageUri)) {
                is ApiResult.Failure -> {
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            submitError = errorMessenger.toUserMessage(imageResult.error),
                        )
                    }
                }

                is ApiResult.Success -> {
                    val input = PredictionInput(
                        imageBytes = imageResult.data.bytes,
                        imageFileName = imageResult.data.fileName,
                        imageMimeType = imageResult.data.mimeType,
                        weight = weight,
                        circumference = circumference,
                    )

                    when (val prediction = predictCitrusUseCase(input)) {
                        is ApiResult.Success -> {
                            val payload = PredictionResultCodec.encode(prediction.data.toUi())
                            _state.update { it.copy(isSubmitting = false, submitError = null) }
                            _effects.emit(PredictionFormEffect.NavigateToResult(payload))
                        }

                        is ApiResult.Failure -> {
                            _state.update {
                                it.copy(
                                    isSubmitting = false,
                                    submitError = errorMessenger.toUserMessage(prediction.error),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
