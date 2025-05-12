package com.example.mypharmacy.view.screens.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.view.components.AlerteItem
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.AlerteViewModel

/**
 * Screen to display and manage alerts
 */
@Composable
fun AlertsScreen(
    navigateToMedicinDetail: (Long) -> Unit,
    viewModel: AlerteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var showOnlyActiveTemp by remember { mutableStateOf(state.showOnlyActive) }
    // Remove explicit type arguments
    var filteredTypeTemp by remember { mutableStateOf(state.filteredType) }

    // Load alerts
    LaunchedEffect(Unit) {
        if (state.showOnlyActive) {
            viewModel.loadActiveAlertes()
        } else {
            viewModel.loadAllAlertes()
        }
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = "Alertes",
                showBackButton = false,
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtres"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Filter chips
                if (state.filteredType != null || !state.showOnlyActive) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filtres actifs:",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Type filter chip
                        state.filteredType?.let { type ->
                            // Use AssistChip instead of FilterChip/ElevatedFilterChip to avoid experimental API
                            IconButton(
                                onClick = { viewModel.clearFilter() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = when(type) {
                                            TypeAlert.STOCK -> Icons.Default.Warning
                                            TypeAlert.EXPIRATION -> Icons.Default.Error
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = when(type) {
                                            TypeAlert.STOCK -> "Stock"
                                            TypeAlert.EXPIRATION -> "Expiration"
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Status filter chip
                        if (!state.showOnlyActive) {
                            // Use IconButton instead of FilterChip to avoid experimental API
                            IconButton(
                                onClick = { viewModel.setShowOnlyActive(true) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Toutes les alertes")
                                }
                            }
                        }
                    }
                }

                if (state.alertes.isEmpty() && !state.isLoading) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Aucune alerte",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (state.showOnlyActive) {
                                "Toutes les alertes ont été résolues. Bravo!"
                            } else {
                                "Aucune alerte n'a été enregistrée."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // Alerts list
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.alertes) { alerte ->
                            AlerteItem(
                                alerte = alerte,
                                onClick = {
                                    // Navigate to medicin detail if needed
                                    alerte.lot?.let { lot ->
                                        navigateToMedicinDetail(lot.medicinId)
                                    }
                                },
                                onResolveClick = {
                                    viewModel.markAlerteAsResolved(alerte.id)
                                }
                            )
                        }
                    }
                }
            }

            // Loading overlay
            LoadingOverlay(isLoading = state.isLoading)

            // Filter dialog
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { showFilterDialog = false },
                    title = { Text("Filtrer les alertes") },
                    text = {
                        Column {
                            // Show resolved alerts toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = showOnlyActiveTemp,
                                    onCheckedChange = { showOnlyActiveTemp = it }
                                )
                                Text("Afficher uniquement les alertes actives")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Alert type filter
                            Text(
                                text = "Type d'alerte:",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // All types option
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filteredTypeTemp == null,
                                    onClick = { filteredTypeTemp = null }
                                )
                                Text("Tous les types")
                            }

                            // Stock alerts option
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filteredTypeTemp == TypeAlert.STOCK,
                                    onClick = { filteredTypeTemp = TypeAlert.STOCK }
                                )
                                Text("Alertes de stock")
                            }

                            // Expiration alerts option
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filteredTypeTemp == TypeAlert.EXPIRATION,
                                    onClick = { filteredTypeTemp = TypeAlert.EXPIRATION }
                                )
                                Text("Alertes d'expiration")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Apply filters
                                viewModel.setShowOnlyActive(showOnlyActiveTemp)

                                filteredTypeTemp?.let {
                                    viewModel.loadAlertesByType(it)
                                } ?: viewModel.clearFilter()

                                showFilterDialog = false
                            }
                        ) {
                            Text("Appliquer")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                // Reset temporary values
                                showOnlyActiveTemp = state.showOnlyActive
                                filteredTypeTemp = state.filteredType
                                showFilterDialog = false
                            }
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
 * A custom radio button with a checkmark icon
 * (Reused from other screens)
 */
@Composable
fun RadioButtonWithCheck(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )
        }
    }
}