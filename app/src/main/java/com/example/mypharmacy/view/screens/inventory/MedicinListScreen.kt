package com.example.mypharmacy.view.screens.inventory

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MedicinItem
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.MedicinViewModel

@Composable
fun MedicinListScreen(
    navigateToDetail: (Long) -> Unit,
    navigateToAddEdit: (Long?) -> Unit,
    viewModel: MedicinViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showLowStockOnly by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Collect all unique categories
    val categories = remember(state.medicins) {
        state.medicins
            .mapNotNull { it.categorie }
            .filter { it.isNotBlank() }
            .toSet()
            .toList()
    }

    // Load medicins
    LaunchedEffect(Unit) {
        viewModel.loadAllMedicins()
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = "Médicaments",
                showBackButton = false,
                actions = {
                    if (showSearchBar) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.setSearchQuery(it.ifBlank { null })
                            },
                            placeholder = { Text("Rechercher...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 48.dp),
                            trailingIcon = {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    viewModel.setSearchQuery(null)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Effacer"
                                    )
                                }
                            }
                        )

                        IconButton(onClick = {
                            showSearchBar = false
                            searchQuery = ""
                            viewModel.setSearchQuery(null)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Fermer recherche"
                            )
                        }
                    } else {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Rechercher"
                            )
                        }

                        IconButton(onClick = { showFilterDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filtres"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddEdit(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un médicament"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.medicins.isEmpty() && !state.isLoading) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Aucun médicament trouvé",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ajoutez votre premier médicament en utilisant le bouton + ci-dessous.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { navigateToAddEdit(null) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ajouter un médicament")
                    }
                }
            } else {
                // Medicin list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.medicins) { medicin ->
                        MedicinItem(
                            medicin = medicin,
                            onClick = { navigateToDetail(medicin.id.toLong()) }
                        )
                    }
                }
            }

            // Loading overlay
            LoadingOverlay(isLoading = state.isLoading)

            // Filter dialog
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { showFilterDialog = false },
                    title = { Text("Filtrer les médicaments") },
                    text = {
                        Column {
                            // Low stock filter
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = showLowStockOnly,
                                    onCheckedChange = { showLowStockOnly = it }
                                )
                                Text("Afficher uniquement le stock bas")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Category filter
                            Text(
                                text = "Catégorie :",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Column {
                                // All categories option
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButtonWithCheck(
                                        selected = selectedCategory == null,
                                        onClick = { selectedCategory = null }
                                    )
                                    Text("Toutes les catégories")
                                }

                                // Individual categories
                                categories.forEach { category ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        RadioButtonWithCheck(
                                            selected = selectedCategory == category,
                                            onClick = { selectedCategory = category }
                                        )
                                        Text(category)
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.setLowStockFilter(showLowStockOnly)
                                viewModel.setCategoryFilter(selectedCategory)
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
                                showLowStockOnly = state.showOnlyLowStock
                                selectedCategory = state.categoryFilter
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