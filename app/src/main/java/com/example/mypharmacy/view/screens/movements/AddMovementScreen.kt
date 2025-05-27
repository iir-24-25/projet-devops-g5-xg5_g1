package com.example.mypharmacy.view.screens.movements

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.MedicinViewModel
import com.example.mypharmacy.viewmodel.MouvementStockViewModel
import java.time.LocalDateTime

/**
 * Screen for adding a new stock movement (Simplified - No Authentication)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovementScreen(
    lotId: Long?,
    navigateUp: () -> Unit,
    viewModel: MouvementStockViewModel = hiltViewModel(),
    medicinViewModel: MedicinViewModel = hiltViewModel()
) {
    val mouvementState by viewModel.state.collectAsState()
    val medicinState by medicinViewModel.state.collectAsState()
    val context = LocalContext.current

    // Form state
    var type by remember { mutableStateOf(TypeMouvement.ENTREE) }
    var motif by remember { mutableStateOf("") }
    var quantite by remember { mutableStateOf("") }
    var selectedMedicinId by remember { mutableIntStateOf(0) }
    var hasNavigated by remember { mutableStateOf(false) }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }

    // Load medicins for selection
    LaunchedEffect(Unit) {
        medicinViewModel.loadAllMedicins()
    }

    // Handle success navigation
    LaunchedEffect(mouvementState.isLoading, mouvementState.error) {
        if (!mouvementState.isLoading && mouvementState.error == null && !hasNavigated) {
            if (motif.isNotBlank() && quantite.isNotBlank() && selectedMedicinId > 0) {
                hasNavigated = true
                Toast.makeText(context, "Mouvement enregistré avec succès", Toast.LENGTH_SHORT).show()
                navigateUp()
            }
        }
    }

    // Handle error messages
    LaunchedEffect(mouvementState.error) {
        mouvementState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = "Nouveau mouvement",
                showBackButton = true,
                onBackClicked = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Validate required fields
                    if (motif.isBlank()) {
                        Toast.makeText(context, "Veuillez saisir un motif", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }

                    if (quantite.isBlank()) {
                        Toast.makeText(context, "Veuillez saisir une quantité", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }

                    if (selectedMedicinId <= 0) {
                        Toast.makeText(context, "Veuillez sélectionner un médicament", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }

                    // Validate quantity
                    val qte = quantite.toIntOrNull()
                    if (qte == null || qte <= 0) {
                        Toast.makeText(context, "Quantité invalide (doit être un nombre positif)", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }

                    // Create movement with default user ID (1L)
                    val mouvement = MouvementStock(
                        motif = motif.trim(),
                        type = type,
                        dateMouvement = LocalDateTime.now(),
                        lotId = selectedMedicinId.toLong(), // Convert Int to Long
                        utilisateurId = 1L, // Default user ID - no authentication needed
                        quantite = qte
                    )

                    hasNavigated = false
                    viewModel.createMouvement(mouvement)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Save, contentDescription = "Enregistrer")
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
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Movement type selection
                Text(
                    text = "Type de mouvement",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = type == TypeMouvement.ENTREE,
                        onClick = { type = TypeMouvement.ENTREE }
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Entrée",
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    RadioButton(
                        selected = type == TypeMouvement.SORTIE,
                        onClick = { type = TypeMouvement.SORTIE }
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = "Sortie",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Medicin selection
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val selectedMedicin = medicinState.medicins.find { it.id == selectedMedicinId }

                    OutlinedTextField(
                        value = selectedMedicin?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Médicament*") },
                        placeholder = { Text("Sélectionnez un médicament") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true,
                        isError = selectedMedicinId <= 0
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (medicinState.medicins.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Aucun médicament disponible",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = { }
                            )
                        } else {
                            medicinState.medicins.forEach { medicin ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = medicin.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "Stock: ${medicin.quantity ?: 0} unités",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedMedicinId = medicin.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Show current stock for selected medicin
                if (selectedMedicinId > 0) {
                    val selectedMedicin = medicinState.medicins.find { it.id == selectedMedicinId }
                    selectedMedicin?.let { medicin ->
                        Text(
                            text = "Stock actuel: ${medicin.quantity ?: 0} unités",
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                (medicin.quantity ?: 0) <= 0 -> MaterialTheme.colorScheme.error
                                (medicin.quantity ?: 0) <= 10 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                }

                // Quantity
                OutlinedTextField(
                    value = quantite,
                    onValueChange = { newValue ->
                        // Only allow numbers
                        if (newValue.isEmpty() || newValue.all { char -> char.isDigit() }) {
                            quantite = newValue
                        }
                    },
                    label = { Text("Quantité*") },
                    placeholder = { Text("Nombre d'unités") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = quantite.isBlank() || quantite.toIntOrNull() == null || (quantite.toIntOrNull() ?: 0) <= 0
                )

                // Motif
                OutlinedTextField(
                    value = motif,
                    onValueChange = { motif = it },
                    label = { Text("Motif*") },
                    placeholder = { Text("Raison du mouvement (ex: Livraison, Vente, Péremption...)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    isError = motif.isBlank()
                )

                // Info section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "* Champs obligatoires",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "ℹ️ Le stock sera automatiquement mis à jour après l'enregistrement",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }

            // Loading overlay
            LoadingOverlay(isLoading = mouvementState.isLoading || medicinState.isLoading)
        }
    }
}