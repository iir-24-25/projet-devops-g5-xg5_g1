package com.example.mypharmacy.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.view.theme.extendedColors

/**
 * A card item displaying medicine information
 *
 * @param medicin The medicine to display
 * @param onClick Called when the card is clicked
 */
@Composable
fun MedicinItem(
    medicin: Medicin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Medicine icon or color indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Medicine information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medicin.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = medicin.fabriquant,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stock status indicator
                if (medicin.quantity != null && medicin.seuilAlerte != null) {
                    val stockPercentage = calculateStockPercentage(medicin)
                    val stockColor = getStockColor(medicin)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Fixed LinearProgressIndicator to match the expected parameter types
                        LinearProgressIndicator(
                            progress = stockPercentage,  // Changed from lambda to direct value
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(MaterialTheme.shapes.small),
                            color = stockColor,
                            trackColor = stockColor.copy(alpha = 0.2f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${medicin.quantity} unités",
                            style = MaterialTheme.typography.bodySmall,
                            color = stockColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Arrow indicator
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Voir détails",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * A compact version of the medicine item for use in lists where space is limited
 */
@Composable
fun MedicinCompactItem(
    medicin: Medicin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stock status indicator
            if (medicin.quantity != null && medicin.seuilAlerte != null) {
                val stockColor = getStockColor(medicin)
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(stockColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Medicine name and quantity
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medicin.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (medicin.quantity != null) {
                    Text(
                        text = "${medicin.quantity} unités",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Voir détails",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Helper functions - not marked as @Composable since they don't need to be
private fun calculateStockPercentage(medicin: Medicin): Float {
    val quantity = medicin.quantity ?: 0
    val threshold = medicin.seuilAlerte ?: 1

    // Calculate a percentage where:
    // - 0% is 0 units
    // - 50% is at the alert threshold
    // - 100% is at 2x the alert threshold

    val targetMax = threshold * 2
    return (quantity.toFloat() / targetMax).coerceIn(0f, 1f)
}

@Composable
private fun getStockColor(medicin: Medicin): Color {
    val quantity = medicin.quantity ?: 0
    val threshold = medicin.seuilAlerte ?: 0

    return when {
        quantity <= 0 -> MaterialTheme.extendedColors.lowStock
        quantity <= threshold -> MaterialTheme.extendedColors.lowStock
        quantity <= threshold * 1.5 -> MaterialTheme.extendedColors.mediumStock
        else -> MaterialTheme.extendedColors.goodStock
    }
}