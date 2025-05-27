package com.example.mypharmacy.view.screens.dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.Alerte
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.view.components.AlerteCompactItem
import com.example.mypharmacy.view.components.LoadingOverlay
import com.example.mypharmacy.view.components.MedicinCompactItem
import com.example.mypharmacy.view.components.MenuItem
import com.example.mypharmacy.view.components.MouvementCompactItem
import com.example.mypharmacy.view.components.MyPharmacyTopAppBar
import com.example.mypharmacy.viewmodel.AlerteViewModel
import com.example.mypharmacy.viewmodel.AuthViewModel
import com.example.mypharmacy.viewmodel.MedicinViewModel
import com.example.mypharmacy.viewmodel.MouvementStockViewModel

@Composable
fun DashboardScreen(
    navigateToMedicins: () -> Unit,
    navigateToMovements: () -> Unit,
    navigateToAlerts: () -> Unit,
    navigateToAddMedicin: () -> Unit,

    navigateToSettings: () -> Unit,
    navigateToLogin: () -> Unit,


    medicinViewModel: MedicinViewModel = hiltViewModel(),
    alerteViewModel: AlerteViewModel = hiltViewModel(),
    mouvementViewModel: MouvementStockViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.state.collectAsState()
    val medicinState by medicinViewModel.state.collectAsState()
    val alerteState by alerteViewModel.state.collectAsState()
    val mouvementState by mouvementViewModel.state.collectAsState()

    // Load data
    LaunchedEffect(Unit) {
        medicinViewModel.loadAllMedicins()
        alerteViewModel.loadActiveAlertes()
        mouvementViewModel.loadAllMouvements()
    }

    // Filter data by current user if needed
    LaunchedEffect(authState.currentUser) {
        authState.currentUser?.let { user ->
            if (user.id > 0) {
                // You can filter by user ID if needed
            }
        }
    }

    Scaffold(
        topBar = {
            MyPharmacyTopAppBar(
                title = "Tableau de bord",
                showMenu = true,
                menuItems = listOf(
                    MenuItem(
                        title = "Paramètres",
                        icon = Icons.Default.Settings,
                        onClick = { navigateToSettings () }
                    ),
                    MenuItem(
                        title = "Déconnexion",
                        icon = Icons.Default.ExitToApp,
                        onClick = { authViewModel.logout()
                            navigateToLogin()
                        }
                    )
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddMedicin,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Welcome message
            item {
                WelcomeSection(
                    username = authState.currentUser?.username ?: "Pharmacist"
                )
            }

            // Quick actions
            item {
                QuickActionsSection(
                    navigateToMedicins = navigateToMedicins,
                    navigateToMovements = navigateToMovements,
                    navigateToAlerts = navigateToAlerts
                )
            }

            // Alerts section
            if (alerteState.alertes.isNotEmpty()) {
                item {
                    SectionTitle(
                        title = "Alertes actives",
                        count = alerteState.alertes.size,
                        onClick = navigateToAlerts
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(alerteState.alertes.take(5)) { alerte ->
                            AlerteCompactItem(
                                alerte = alerte,
                                onClick = navigateToAlerts,
                                modifier = Modifier.width(280.dp)
                            )
                        }

                        if (alerteState.alertes.size > 5) {
                            item {
                                ViewMoreCard(
                                    text = "Voir toutes les alertes",
                                    onClick = navigateToAlerts
                                )
                            }
                        }
                    }
                }
            }

            // Recent medicines
            if (medicinState.medicins.isNotEmpty()) {
                item {
                    SectionTitle(
                        title = "Médicaments récents",
                        count = medicinState.medicins.size,
                        onClick = navigateToMedicins
                    )
                }

                items(medicinState.medicins.take(3)) { medicin ->
                    MedicinCompactItem(
                        medicin = medicin,
                        onClick = {
                            medicinViewModel.selectMedicin(medicin)
                            navigateToMedicins()
                        }
                    )
                }

                if (medicinState.medicins.size > 3) {
                    item {
                        ViewMoreButton(
                            text = "Voir tous les médicaments",
                            onClick = navigateToMedicins
                        )
                    }
                }
            }

            // Recent movements
            if (mouvementState.mouvements.isNotEmpty()) {
                item {
                    SectionTitle(
                        title = "Mouvements récents",
                        count = mouvementState.mouvements.size,
                        onClick = navigateToMovements
                    )
                }

                items(mouvementState.mouvements.take(3)) { mouvement ->
                    MouvementCompactItem(
                        mouvement = mouvement,
                        onClick = {
                            mouvementViewModel.selectMouvement(mouvement)
                            navigateToMovements()
                        }
                    )
                }

                if (mouvementState.mouvements.size > 3) {
                    item {
                        ViewMoreButton(
                            text = "Voir tous les mouvements",
                            onClick = navigateToMovements
                        )
                    }
                }
            }
        }

        // Loading overlay
        LoadingOverlay(
            isLoading = medicinState.isLoading || alerteState.isLoading || mouvementState.isLoading
        )
    }
}

@Composable
fun WelcomeSection(username: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Bonjour, $username",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bienvenue dans votre tableau de bord",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickActionsSection(
    navigateToMedicins: () -> Unit,
    navigateToMovements: () -> Unit,
    navigateToAlerts: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Accès rapide",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionItem(
                icon = Icons.Default.LocalPharmacy,
                label = "Médicaments",
                onClick = navigateToMedicins,
                modifier = Modifier.weight(1f)
            )

            QuickActionItem(
                icon = Icons.Default.MoveDown,
                label = "Mouvements",
                onClick = navigateToMovements,
                modifier = Modifier.weight(1f)
            )

            QuickActionItem(
                icon = Icons.Default.Notifications,
                label = "Alertes",
                onClick = navigateToAlerts,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "($count)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Voir tout",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@Composable
fun ViewMoreButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(8.dp)
        )
    }
}

@Composable
fun ViewMoreCard(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}