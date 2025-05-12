package com.example.mypharmacy.view.screens.movements

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MouvementItem
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.MouvementStockViewModel

/**
 * Screen to display stock movements
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementListScreen(
    navigateToAddMovement: (Long?) -> Unit,
    viewModel: MouvementStockViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf<TypeMouvement?>(null) }

    // Load movements
    LaunchedEffect(Unit) {
        viewModel.loadAllMouvements()
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = "Mouvements de stock",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddMovement(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un mouvement"
                )
            }
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
                if (state.filteredType != null) {
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

                        // Type filter chip - replaced with ElevatedFilterChip to avoid experimental API
                        state.filteredType?.let { type ->
                            ElevatedFilterChip(
                                selected = true,
                                onClick = {
                                    viewModel.clearFilters()
                                    filterType = null
                                },
                                label = {
                                    Text(
                                        text = when(type) {
                                            TypeMouvement.ENTREE -> "Entrées"
                                            TypeMouvement.SORTIE -> "Sorties"
                                        }
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when(type) {
                                            TypeMouvement.ENTREE -> Icons.Default.ArrowUpward
                                            TypeMouvement.SORTIE -> Icons.Default.ArrowDownward
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp) // Fixed size instead of FilterChipDefaults.IconSize
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Supprimer filtre",
                                        modifier = Modifier.size(24.dp) // Fixed size instead of FilterChipDefaults.IconSize
                                    )
                                }
                            )
                        }
                    }
                }

                if (state.mouvements.isEmpty() && !state.isLoading) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Aucun mouvement de stock",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Ajoutez votre premier mouvement en utilisant le bouton + ci-dessous.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // Movements list
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.mouvements) { mouvement ->
                            MouvementItem(
                                mouvement = mouvement,
                                onClick = { /* Navigate to detail if needed */ }
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
                    title = { Text("Filtrer les mouvements") },
                    text = {
                        Column {
                            Text(
                                text = "Type de mouvement:",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Type options
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filterType == null,
                                    onClick = { filterType = null }
                                )
                                Text("Tous les types")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filterType == TypeMouvement.ENTREE,
                                    onClick = { filterType = TypeMouvement.ENTREE }
                                )
                                Text("Entrées")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButtonWithCheck(
                                    selected = filterType == TypeMouvement.SORTIE,
                                    onClick = { filterType = TypeMouvement.SORTIE }
                                )
                                Text("Sorties")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                filterType?.let {
                                    viewModel.loadMouvementsByType(it)
                                } ?: viewModel.loadAllMouvements()
                                showFilterDialog = false
                            }
                        ) {
                            Text("Appliquer")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                // Reset to previous values
                                filterType = state.filteredType
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
 * (Reused from MedicinListScreen)
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
                imageVector = Icons.Default.Add, // Replace with checkmark icon
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