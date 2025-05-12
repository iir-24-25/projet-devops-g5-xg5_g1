package com.example.mypharmacy.view.screens.inventory

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MenuItem
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.view.theme.extendedColors
import com.example.mypharmacy.viewmodel.MedicinViewModel

/**
 * Screen showing the details of a medication
 */
@Composable
fun MedicinDetailScreen(
    medicinId: Long,
    navigateUp: () -> Unit,
    navigateToEdit: () -> Unit,
    navigateToAddMovement: (Long) -> Unit,
    navigateToDetail: (Long) -> Unit, // Nouveau paramètre ajouté
    viewModel: MedicinViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Load medicin by ID
    LaunchedEffect(medicinId) {
        // Find the medicin in the existing list or reload if needed
        val foundMedicin = state.medicins.find { it.id.toLong() == medicinId }
        if (foundMedicin != null) {
            viewModel.selectMedicin(foundMedicin)
        } else {
            // If not found, we might need to load medicins first
            // This depends on your data loading strategy
            // You could call viewModel.loadAllMedicins() here
        }
    }

    val medicin = state.selectedMedicin

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = medicin?.name ?: "Détail Médicament",
                showBackButton = true,
                onBackClicked = navigateUp,
                menuItems = listOf(
                    MenuItem(
                        title = "Modifier",
                        icon = Icons.Default.Edit,
                        onClick = navigateToEdit
                    ),
                    MenuItem(
                        title = "Supprimer",
                        icon = Icons.Default.Delete,
                        onClick = { showDeleteConfirmation = true }
                    )
                )
            )
        },
        floatingActionButton = {
            // Show FAB only if we have a valid medicin
            medicin?.let {
                ExtendedFloatingActionButton(
                    onClick = {
                        // For now, we'll just pass 0 as the lot ID
                        // In a real implementation, you'd let the user choose a lot
                        navigateToAddMovement(0)
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Mouvement") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Content
            if (medicin != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Stock status
                    StockStatusCard(medicin = medicin)

                    Spacer(modifier = Modifier.height(16.dp))

                    // General information
                    InfoCard(
                        title = "Informations générales",
                        content = {
                            InfoRow(label = "Nom", value = medicin.name)
                            InfoRow(label = "Fabricant", value = medicin.fabriquant)
                            InfoRow(label = "Catégorie", value = medicin.categorie ?: "Non spécifiée")
                            if (!medicin.codeBarres.isNullOrBlank()) {
                                InfoRow(label = "Code-barres", value = medicin.codeBarres)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    InfoCard(
                        title = "Description",
                        content = {
                            Text(
                                text = medicin.description.ifEmpty { "Aucune description disponible" },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )

                    // TODO: Add lots section when implemented
                    Spacer(modifier = Modifier.height(16.dp))

                    // Placeholder for lots
                    InfoCard(
                        title = "Lots",
                        content = {
                            Text(
                                text = "Aucun lot disponible",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            } else {
                // Loading or not found state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!state.isLoading) {
                        Text(
                            text = "Médicament non trouvé",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Loading overlay
            LoadingOverlay(isLoading = state.isLoading)

            // Delete confirmation dialog
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Supprimer le médicament") },
                    text = { Text("Êtes-vous sûr de vouloir supprimer ce médicament ? Cette action est irréversible.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                medicin?.let {
                                    viewModel.deleteMedicin(it)
                                    Toast.makeText(context, "Médicament supprimé", Toast.LENGTH_SHORT).show()
                                    navigateUp()
                                }
                                showDeleteConfirmation = false
                            }
                        ) {
                            Text("Supprimer")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteConfirmation = false }
                        ) {
                            Text("Annuler")
                        }
                    }
                )
            }
        }
    }
}

/**
 * Card displaying the stock status
 */
@Composable
fun StockStatusCard(medicin: Medicin) {
    val stockLevel = if (medicin.quantity != null && medicin.seuilAlerte != null) {
        when {
            medicin.quantity <= 0 -> Triple("Rupture de stock", MaterialTheme.extendedColors.lowStock, 0f)
            medicin.quantity <= medicin.seuilAlerte -> Triple("Stock bas", MaterialTheme.extendedColors.lowStock, 0.3f)
            medicin.quantity <= medicin.seuilAlerte * 2 -> Triple("Stock moyen", MaterialTheme.extendedColors.mediumStock, 0.6f)
            else -> Triple("Stock bon", MaterialTheme.extendedColors.goodStock, 1f)
        }
    } else {
        Triple("Stock non défini", MaterialTheme.colorScheme.onSurfaceVariant, 0f)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    color = stockLevel.second.copy(alpha = 0.2f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = stockLevel.second,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stockLevel.first,
                        style = MaterialTheme.typography.titleMedium,
                        color = stockLevel.second
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${medicin.quantity ?: 0} unités en stock",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (medicin.seuilAlerte != null) {
                        Text(
                            text = "Seuil d'alerte: ${medicin.seuilAlerte} unités",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = stockLevel.third,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                color = stockLevel.second,
                trackColor = stockLevel.second.copy(alpha = 0.2f)
            )
        }
    }
}

/**
 * Card for displaying information sections
 */
@Composable
fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

/**
 * Row for displaying a label and its value
 */
@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}