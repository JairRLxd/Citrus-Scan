package com.example.citrusscan.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.citrusscan.ui.theme.CitrusCard
import com.example.citrusscan.ui.theme.CitrusCream
import com.example.citrusscan.ui.theme.CitrusGold
import com.example.citrusscan.ui.theme.CitrusLeaf
import com.example.citrusscan.ui.theme.CitrusOrange
import com.example.citrusscan.ui.theme.CitrusPeel
import com.example.citrusscan.ui.theme.CitrusText

@Composable
fun HomeScreen(
    onStartAnalysis: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(
        containerColor = CitrusCard,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CitrusCard)
                .padding(padding),
        ) {
            DecorativeGlow(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 48.dp, y = (-12).dp),
            )

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = CitrusText,
                                fontWeight = FontWeight.ExtraBold,
                            ),
                        ) {
                            append("Citrus")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = CitrusOrange,
                                fontWeight = FontWeight.ExtraBold,
                            ),
                        ) {
                            append("Scan")
                        }
                    },
                    style = MaterialTheme.typography.displaySmall,
                )

                Box(
                    modifier = Modifier
                        .size(width = 22.dp, height = 3.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(CitrusOrange),
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = CitrusText, fontWeight = FontWeight.Bold)) {
                            append("Detecta, Analiza y ")
                        }
                        withStyle(SpanStyle(color = CitrusOrange, fontWeight = FontWeight.Bold)) {
                            append("Predice")
                        }
                    },
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CitrusText.copy(alpha = 0.8f),
                )

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White.copy(alpha = 0.94f),
                    tonalElevation = 2.dp,
                    shadowElevation = 6.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        HomeStepCard(
                            stepNumber = "1.",
                            title = "Toma una foto",
                            description = "Captura el citrico que quieres analizar.",
                            icon = Icons.Rounded.PhotoCamera,
                            accent = CitrusOrange,
                            iconBackground = CitrusPeel,
                        )
                        HomeStepCard(
                            stepNumber = "2.",
                            title = "Obten informacion",
                            description = "Recibe datos sobre su estado de forma rapida y sencilla.",
                            icon = Icons.Rounded.Eco,
                            accent = CitrusLeaf,
                            iconBackground = Color(0xFFE8F4E8),
                        )
                        HomeStepCard(
                            stepNumber = "3.",
                            title = "Recibe una prediccion",
                            description = "Obtien resultados para apoyar mejores decisiones.",
                            icon = Icons.Rounded.Analytics,
                            accent = Color(0xFF6EA8FF),
                            iconBackground = Color(0xFFEAF3FF),
                        )

                        Button(
                            onClick = onStartAnalysis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CitrusOrange,
                                contentColor = Color.White,
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
                                Text(
                                    text = "Analizar fruta",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeStepCard(
    stepNumber: String,
    title: String,
    description: String,
    icon: ImageVector,
    accent: Color,
    iconBackground: Color,
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CitrusCream.copy(alpha = 0.56f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "$stepNumber $title",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CitrusText,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CitrusText.copy(alpha = 0.74f),
                )
            }
        }
    }
}

@Composable
private fun DecorativeGlow(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(150.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(132.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CitrusGold.copy(alpha = 0.28f),
                            CitrusOrange.copy(alpha = 0.12f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(width = 76.dp, height = 110.dp)
                .clip(RoundedCornerShape(topStart = 90.dp, bottomStart = 90.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CitrusPeel.copy(alpha = 0.3f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}
