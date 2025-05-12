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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.AuthViewModel
import com.example.mypharmacy.viewmodel.MedicinViewModel

/**
 * Screen for adding or editing a medication
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedicinScreen(
    medicinId: Long?,
    navigateUp: () -> Unit,
    viewModel: MedicinViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val medicinState by viewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()
    val context = LocalContext.current

    // Form state
    var name by remember { mutableStateOf("") }
    var fabriquant by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var codeBarres by remember { mutableStateOf("") }
    var categorie by remember { mutableStateOf("") }
    var seuilAlerte by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    // Category dropdown state
    var expanded by remember { mutableStateOf(false) }

    // Common categories (could be loaded from a repository)
    val categories = listOf(
        "Analgésique", "Antibiotique", "Antidépresseur", "Antihistaminique",
        "Anti-inflammatoire", "Antiviral", "Cardiovasculaire", "Dermatologique",
        "Diabète", "Gastro-intestinal", "Hormonal", "Respiratoire",
        "Vitamine/Supplément", "Autre"
    )

    // Load medicin data if editing
    LaunchedEffect(medicinId) {
        medicinId?.let { id ->
            // Find medicin in current state
            medicinState.medicins.find { it.id.toLong() == id }?.let { medicin ->
                // Update form fields
                name = medicin.name
                fabriquant = medicin.fabriquant
                description = medicin.description
                codeBarres = medicin.codeBarres ?: ""
                categorie = medicin.categorie ?: ""
                seuilAlerte = medicin.seuilAlerte?.toString() ?: ""
                quantity = medicin.quantity?.toString() ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = if (medicinId != null) "Modifier médicament" else "Ajouter médicament",
                showBackButton = true,
                onBackClicked = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Validate required fields
                    if (name.isBlank() || fabriquant.isBlank()) {
                        Toast.makeText(context, "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }

                    // Create or update medicin
                    val medicin = Medicin(
                        id = medicinId?.toInt() ?: 0,
                        name = name,
                        fabriquant = fabriquant,
                        description = description,
                        codeBarres = codeBarres.takeIf { it.isNotBlank() },
                        categorie = categorie.takeIf { it.isNotBlank() },
                        seuilAlerte = seuilAlerte.toIntOrNull(),
                        quantity = quantity.toIntOrNull(),
                        userId = authState.currentUser?.id?.toString() ?: "unknown"
                    )

                    if (medicinId != null) {
                        // Update existing
                        viewModel.updateMedicin(medicin)
                        Toast.makeText(context, "Médicament mis à jour", Toast.LENGTH_SHORT).show()
                    } else {
                        // Create new
                        viewModel.createMedicin(medicin)
                        Toast.makeText(context, "Médicament créé", Toast.LENGTH_SHORT).show()
                    }

                    navigateUp()
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
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom*") },
                    placeholder = { Text("Nom du médicament") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Fabricant
                OutlinedTextField(
                    value = fabriquant,
                    onValueChange = { fabriquant = it },
                    label = { Text("Fabricant*") },
                    placeholder = { Text("Fabricant du médicament") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Category dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = categorie,
                        onValueChange = { categorie = it },
                        label = { Text("Catégorie") },
                        placeholder = { Text("Sélectionnez une catégorie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    categorie = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Code-barres
                OutlinedTextField(
                    value = codeBarres,
                    onValueChange = { codeBarres = it },
                    label = { Text("Code-barres") },
                    placeholder = { Text("Code-barres du médicament") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Quantity and threshold
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Quantity
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantité") },
                        placeholder = { Text("0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Alert threshold
                    OutlinedTextField(
                        value = seuilAlerte,
                        onValueChange = { seuilAlerte = it },
                        label = { Text("Seuil d'alerte") },
                        placeholder = { Text("0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Description du médicament") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                // Required fields note
                Text(
                    text = "* Champs obligatoires",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }

            // Loading overlay
            LoadingOverlay(isLoading = medicinState.isLoading)
        }
    }
}