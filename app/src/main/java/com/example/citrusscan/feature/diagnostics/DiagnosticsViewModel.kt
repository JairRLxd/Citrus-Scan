package com.example.citrusscan.feature.diagnostics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citrusscan.core.common.ApiResult
import com.example.citrusscan.core.common.ErrorMessenger
import com.example.citrusscan.domain.repository.HealthRepository
import com.example.citrusscan.domain.repository.ModelsRepository
import com.example.citrusscan.domain.repository.PreprocessingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    private val healthRepository: HealthRepository,
    private val modelsRepository: ModelsRepository,
    private val preprocessingRepository: PreprocessingRepository,
    private val errorMessenger: ErrorMessenger,
) : ViewModel() {

    private val _state = MutableStateFlow(DiagnosticsUiState())
    val state: StateFlow<DiagnosticsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = DiagnosticsUiState(isLoading = true)

            val healthDeferred = async { healthRepository.getHealth() }
            val modelsDeferred = async { modelsRepository.getModels() }
            val preprocessingDeferred = async { preprocessingRepository.getStatus() }

            val healthResult = healthDeferred.await()
            val modelsResult = modelsDeferred.await()
            val preprocessingResult = preprocessingDeferred.await()

            _state.update {
                it.copy(
                    isLoading = false,
                    health = (healthResult as? ApiResult.Success)?.data,
                    models = (modelsResult as? ApiResult.Success)?.data.orEmpty(),
                    preprocessingStatus = (preprocessingResult as? ApiResult.Success)?.data,
                    healthError = (healthResult as? ApiResult.Failure)?.let { failure ->
                        errorMessenger.toUserMessage(failure.error)
                    },
                    modelsError = (modelsResult as? ApiResult.Failure)?.let { failure ->
                        errorMessenger.toUserMessage(failure.error)
                    },
                    preprocessingError = (preprocessingResult as? ApiResult.Failure)?.let { failure ->
                        errorMessenger.toUserMessage(failure.error)
                    },
                )
            }
        }
    }
}
