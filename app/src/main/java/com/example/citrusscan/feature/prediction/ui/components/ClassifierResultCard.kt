package com.example.citrusscan.feature.prediction.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.citrusscan.feature.prediction.state.ClassifierCardUiModel
import com.example.citrusscan.feature.prediction.state.StatusKind

@Composable
fun ClassifierResultCard(
    card: ClassifierCardUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = card.classifier,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    card.predictedLabel?.let {
                        Text(
                            text = "Prediccion: $it",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Text(
                    text = card.statusLabel,
                    modifier = Modifier
                        .background(
                            color = statusColor(card.statusKind).copy(alpha = 0.14f),
                            shape = RoundedCornerShape(999.dp),
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    color = statusColor(card.statusKind),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            if (card.confidenceRatio != null && card.confidencePercent != null) {
                ConfidenceBar(
                    ratio = card.confidenceRatio,
                    percent = card.confidencePercent,
                )
            }

            if (card.probabilities.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    card.probabilities.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(row.label, style = MaterialTheme.typography.bodyMedium)
                            Text(row.percent, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            card.detail?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun statusColor(kind: StatusKind): Color = when (kind) {
    StatusKind.Success -> MaterialTheme.colorScheme.secondary
    StatusKind.Warning -> MaterialTheme.colorScheme.tertiary
    StatusKind.Error -> MaterialTheme.colorScheme.error
    StatusKind.Unknown -> MaterialTheme.colorScheme.onSurfaceVariant
}
