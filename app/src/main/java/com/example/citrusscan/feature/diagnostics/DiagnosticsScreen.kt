package com.example.citrusscan.feature.diagnostics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.citrusscan.domain.model.ClassifierName
import com.example.citrusscan.ui.theme.CitrusPeel
import com.example.citrusscan.ui.theme.CitrusText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(
    onBack: () -> Unit,
    viewModel: DiagnosticsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(
        containerColor = Color(0xFF101014),
        topBar = {
            TopAppBar(
                title = { Text("Diagnosticos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Actualizar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101014),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CitrusPeel.copy(alpha = 0.35f),
                ) {
                    Text(
                        text = "Estado de servicios y modelos del backend.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CitrusText.copy(alpha = 0.88f),
                        modifier = Modifier.padding(14.dp),
                    )
                }
            }

            item {
                DiagnosticsCard(
                    title = "Health",
                    body = buildString {
                        when {
                            state.health != null -> {
                                appendLine(if (state.health.isUp) "Servidor disponible" else "Servidor reporta fallo")
                                appendLine("Version: ${state.health.version ?: "-"}")
                                append("Uptime: ${state.health.uptimeSeconds ?: "-"}")
                            }

                            state.healthError != null -> append(state.healthError)
                            else -> append(if (state.isLoading) "Cargando..." else "Sin datos.")
                        }
                    },
                )
            }

            item {
                DiagnosticsCard(
                    title = "Preprocessing",
                    body = buildString {
                        when {
                            state.preprocessingStatus != null -> {
                                appendLine("Ready: ${state.preprocessingStatus.ready}")
                                appendLine("Pipeline: ${state.preprocessingStatus.pipeline.joinToString()}")
                                append("Detalle: ${state.preprocessingStatus.detail ?: "-"}")
                            }

                            state.preprocessingError != null -> append(state.preprocessingError)
                            else -> append(if (state.isLoading) "Cargando..." else "Sin datos.")
                        }
                    },
                )
            }

            item {
                Text(
                    text = "Modelos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (state.models.isNotEmpty()) {
                items(state.models) { model ->
                    DiagnosticsCard(
                        title = model.classifier.toDisplayName(),
                        body = buildString {
                            appendLine("Entrenado: ${model.trained}")
                            appendLine("Accuracy: ${model.accuracy ?: "-"}")
                            append("Ultimo entrenamiento: ${model.lastTrainedAt ?: "-"}")
                        },
                    )
                }
            } else {
                item {
                    DiagnosticsCard(
                        title = "Estado",
                        body = state.modelsError ?: if (state.isLoading) "Cargando modelos..." else "No hay modelos.",
                    )
                }
            }
        }
    }
}

@Composable
private fun DiagnosticsCard(
    title: String,
    body: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun ClassifierName.toDisplayName(): String = when (this) {
    ClassifierName.SVM -> "SVM"
    ClassifierName.BAYES -> "Bayes"
    ClassifierName.PERCEPTRON -> "Perceptron"
    ClassifierName.UNKNOWN -> "Desconocido"
}
