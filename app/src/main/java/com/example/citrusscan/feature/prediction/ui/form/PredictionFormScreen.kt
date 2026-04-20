package com.example.citrusscan.feature.prediction.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.citrusscan.feature.prediction.state.PredictionFormEffect
import com.example.citrusscan.feature.prediction.state.PredictionUiEvent
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionFormScreen(
    initialImageUri: String?,
    onBack: () -> Unit,
    onShowResult: (String) -> Unit,
    viewModel: PredictionFormViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(initialImageUri) {
        initialImageUri?.let { viewModel.onEvent(PredictionUiEvent.ImagePicked(it)) }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PredictionFormEffect.NavigateToResult -> onShowResult(effect.payload)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de analisis") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
            ) {
                AsyncImage(
                    model = state.selectedImageUri,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )
            }

            OutlinedTextField(
                value = state.weight,
                onValueChange = { viewModel.onEvent(PredictionUiEvent.WeightChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Peso (gr)") },
                supportingText = { state.weightError?.let { Text(it) } },
                isError = state.weightError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) },
                ),
            )

            OutlinedTextField(
                value = state.circumference,
                onValueChange = { viewModel.onEvent(PredictionUiEvent.CircumferenceChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Circunferencia (cm)") },
                supportingText = { state.circumferenceError?.let { Text(it) } },
                isError = state.circumferenceError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = if (state.isSubmitting) ImeAction.None else ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (state.isSubmitting) return@KeyboardActions
                        keyboardController?.hide()
                        viewModel.onEvent(PredictionUiEvent.Submit)
                    },
                ),
            )

            state.submitError?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isValid,
                onClick = { viewModel.onEvent(PredictionUiEvent.Submit) },
            ) {
                Text(if (state.isSubmitting) "Analizando..." else "Analizar")
            }
        }
    }
}
