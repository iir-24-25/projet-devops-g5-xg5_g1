package com.example.mypharmacy.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material.icons.outlined.MoveDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Enum representing the different screens in the application
 * Each screen has a route, title, and icons (selected and unselected)
 */
enum class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val showInBottomNav: Boolean = true,
    val requiresAuth: Boolean = true
) {
    // Authentication screens (not shown in bottom nav)
    Login("login", "Connexion", Icons.Filled.Person, Icons.Outlined.Person, false, false),
    Register("register", "Inscription", Icons.Filled.Person, Icons.Outlined.Person, false, false),

    // Main dashboard
    Dashboard("dashboard", "Tableau de bord", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),

    // Inventory screens
    MedicinList("medicin_list", "Médicaments", Icons.Filled.LocalPharmacy, Icons.Outlined.LocalPharmacy),
    MedicinDetail("medicin_detail/{medicinId}", "Détail Médicament", Icons.Filled.LocalPharmacy, Icons.Outlined.LocalPharmacy, false),
    AddEditMedicin("add_edit_medicin?medicinId={medicinId}", "Ajouter/Modifier Médicament", Icons.Filled.LocalPharmacy, Icons.Outlined.LocalPharmacy, false),

    // Movement screens
    MovementList("movement_list", "Mouvements", Icons.Filled.MoveDown, Icons.Outlined.MoveDown),
    AddMovement("add_movement?lotId={lotId}", "Nouveau Mouvement", Icons.Filled.MoveDown, Icons.Outlined.MoveDown, false),

    // Alert screens
    Alerts("alerts", "Alertes", Icons.Filled.Notifications, Icons.Outlined.Notifications),

    // Admin screens
    Admin("admin", "Administration", Icons.Filled.Person, Icons.Outlined.Person),

    // Logs screen
    Logs("logs", "Logs", Icons.Filled.Report, Icons.Outlined.Report),

    // Settings screen - NOUVEAU
    Settings("settings", "Paramètres", Icons.Filled.Settings, Icons.Outlined.Settings, false);

    companion object {
        fun fromRoute(route: String): Screen {
            return when {
                route.startsWith("medicin_detail/") -> MedicinDetail
                route.startsWith("add_edit_medicin") -> AddEditMedicin
                route.startsWith("add_movement") -> AddMovement
                else -> values().find { it.route == route } ?: Dashboard
            }
        }

        fun getBottomNavItems(): List<Screen> {
            return values().filter { it.showInBottomNav }
        }
    }
}