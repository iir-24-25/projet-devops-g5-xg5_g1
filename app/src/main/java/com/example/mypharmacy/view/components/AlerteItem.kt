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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypharmacy.model.domain.Alerte
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.view.theme.extendedColors
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Item card for displaying an alert
 *
 * @param alerte The alert to display
 * @param onClick Called when the card is clicked
 * @param onResolveClick Called when the resolve button is clicked
 */
@Composable
fun AlerteItem(
    alerte: Alerte,
    onClick: () -> Unit,
    onResolveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, iconVector, iconTint) = when (alerte.type) {
        TypeAlert.STOCK -> Triple(
            MaterialTheme.extendedColors.warningContainer,
            Icons.Default.Warning,
            MaterialTheme.extendedColors.warningColor
        )
        TypeAlert.EXPIRATION -> Triple(
            MaterialTheme.extendedColors.errorContainer ?: MaterialTheme.colorScheme.errorContainer,
            Icons.Default.Error,
            MaterialTheme.colorScheme.error
        )
    }

    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                .background(backgroundColor.copy(alpha = 0.2f))
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Alert icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .border(
                        width = 1.dp,
                        color = iconTint.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Alert content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title - based on alert type
                Text(
                    text = when (alerte.type) {
                        TypeAlert.STOCK -> "Alerte de stock"
                        TypeAlert.EXPIRATION -> "Alerte d'expiration"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = iconTint
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Alert message
                Text(
                    text = alerte.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date
                Text(
                    text = "Créée le ${alerte.dateAlerte.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resolve button (only shown if not resolved)
                if (!alerte.estResolue) {
                    Button(
                        onClick = onResolveClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Marquer comme résolu")
                    }
                } else {
                    Text(
                        text = "Résolu",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.extendedColors.successColor,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

/**
 * Compact version of the alert item for use in lists or summary views
 */
@Composable
fun AlerteCompactItem(
    alerte: Alerte,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, iconVector, iconTint) = when (alerte.type) {
        TypeAlert.STOCK -> Triple(
            MaterialTheme.extendedColors.warningContainer,
            Icons.Default.Warning,
            MaterialTheme.extendedColors.warningColor
        )
        TypeAlert.EXPIRATION -> Triple(
            MaterialTheme.extendedColors.errorContainer ?: MaterialTheme.colorScheme.errorContainer,
            Icons.Default.Error,
            MaterialTheme.colorScheme.error
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = alerte.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (alerte.estResolue) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Résolu",
                    tint = MaterialTheme.extendedColors.successColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}