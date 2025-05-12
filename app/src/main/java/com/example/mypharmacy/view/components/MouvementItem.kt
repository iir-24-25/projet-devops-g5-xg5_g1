package com.example.mypharmacy.view.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.model.domain.TypeMouvement
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Helper class to handle multiple return values
 */
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

/**
 * Item card for displaying a stock movement
 *
 * @param mouvement The stock movement to display
 * @param onClick Called when the card is clicked
 * @param showLot Whether to show the lot information
 */
@Composable
fun MouvementItem(
    mouvement: MouvementStock,
    onClick: () -> Unit,
    showLot: Boolean = true,
    modifier: Modifier = Modifier
) {
    val (iconVector, iconTint, backgroundColor, sign) = when (mouvement.type) {
        TypeMouvement.ENTREE -> Quadruple(
            Icons.Default.ArrowUpward,
            Color(0xFF4CAF50), // Green
            Color(0xFFE8F5E9), // Light green background
            "+"
        )
        TypeMouvement.SORTIE -> Quadruple(
            Icons.Default.ArrowDownward,
            Color(0xFFF44336), // Red
            Color(0xFFFFEBEE), // Light red background
            "-"
        )
    }

    val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)

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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Movement type icon
            Box(
                modifier = Modifier
                    .size(48.dp)
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

            // Movement information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Movement type text
                    Text(
                        text = when (mouvement.type) {
                            TypeMouvement.ENTREE -> "Entrée de stock"
                            TypeMouvement.SORTIE -> "Sortie de stock"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Quantity with sign
                    Text(
                        text = "$sign${mouvement.quantite} unités",
                        style = MaterialTheme.typography.titleMedium,
                        color = iconTint,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Motif
                Text(
                    text = mouvement.motif,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date and user info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = mouvement.dateMouvement.format(dateFormatter),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // User
                    mouvement.utilisateur?.let { user ->
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = user.username,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Lot information
                if (showLot && mouvement.lot != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                MaterialTheme.shapes.small
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lot: ${mouvement.lot.numeroLot}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Exp: ${mouvement.lot.dateExpiration.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact version of the movement item for use in lists or summary views
 */
@Composable
fun MouvementCompactItem(
    mouvement: MouvementStock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (iconVector, iconTint, _, sign) = when (mouvement.type) {
        TypeMouvement.ENTREE -> Quadruple(
            Icons.Default.ArrowUpward,
            Color(0xFF4CAF50), // Green
            Color(0xFFE8F5E9), // Light green background
            "+"
        )
        TypeMouvement.SORTIE -> Quadruple(
            Icons.Default.ArrowDownward,
            Color(0xFFF44336), // Red
            Color(0xFFFFEBEE), // Light red background
            "-"
        )
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mouvement.motif,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = mouvement.dateMouvement.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$sign${mouvement.quantite}",
                style = MaterialTheme.typography.titleSmall,
                color = iconTint
            )
        }
    }
}