package com.example.citrusscan.feature.prediction.ui.result

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.citrusscan.feature.prediction.state.PredictionResultUiState
import com.example.citrusscan.feature.prediction.ui.components.BestResultBanner
import com.example.citrusscan.feature.prediction.ui.components.ClassifierResultCard
import com.example.citrusscan.ui.theme.CitrusText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionResultScreen(
    payload: String?,
    onBack: () -> Unit,
    onRestart: () -> Unit,
    viewModel: PredictionResultViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(payload) {
        viewModel.load(payload)
    }

    Scaffold(
        containerColor = Color(0xFF101014),
        topBar = {
            TopAppBar(
                title = { Text("Resultado") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101014),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
    ) { padding ->
        Crossfade(
            targetState = state,
            label = "result_state",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val current = it) {
                PredictionResultUiState.Idle,
                PredictionResultUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Procesando resultados...",
                            modifier = Modifier.padding(top = 12.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                is PredictionResultUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.errorContainer,
                        ) {
                            Text(
                                text = current.message,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                        }
                        Button(
                            onClick = onRestart,
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text("Volver al inicio")
                        }
                    }
                }

                is PredictionResultUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            BestResultBanner(best = current.data.best)
                        }
                        items(current.data.cards) { card ->
                            ClassifierResultCard(card = card, modifier = Modifier.fillMaxWidth())
                        }
                        item {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onRestart,
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text("Nuevo analisis")
                            }
                        }
                    }
                }
            }
        }
    }
}
