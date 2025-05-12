package com.example.mypharmacy.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mypharmacy.view.screens.admin.AdminScreen
import com.example.mypharmacy.view.screens.alerts.AlertsScreen
import com.example.mypharmacy.view.screens.auth.LoginScreen
import com.example.mypharmacy.view.screens.auth.RegisterScreen
import com.example.mypharmacy.view.screens.dashboard.DashboardScreen
import com.example.mypharmacy.view.screens.inventory.AddEditMedicinScreen
import com.example.mypharmacy.view.screens.inventory.MedicinDetailScreen
import com.example.mypharmacy.view.screens.inventory.MedicinListScreen
import com.example.mypharmacy.view.screens.logs.LogsScreen
import com.example.mypharmacy.view.screens.movements.AddMovementScreen
import com.example.mypharmacy.view.screens.movements.MovementListScreen
import com.example.mypharmacy.viewmodel.AuthViewModel
import com.example.mypharmacy.view.screens.settings.SettingsScreen

private const val ANIMATION_DURATION = 300

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    val authState by authViewModel.state.collectAsState()

    // Check if the user is logged in to determine the start destination
    LaunchedEffect(key1 = authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route in listOf(Screen.Login.route, Screen.Register.route)) {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        } else if (startDestination != Screen.Login.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToRegister = { navController.navigate(Screen.Register.route) },
                navigateToDashboard = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                navigateToLogin = { navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }}
            )
        }

        // Main screens
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navigateToMedicins = { navController.navigate(Screen.MedicinList.route) },
                navigateToMovements = { navController.navigate(Screen.MovementList.route) },
                navigateToAlerts = { navController.navigate(Screen.Alerts.route) },
                navigateToAddMedicin = { navController.navigate(Screen.AddEditMedicin.route.replace("{medicinId}", "")) },
                navigateToSettings = { navController.navigate(Screen.Settings.route) },
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        // Medicin screens
        composable(Screen.MedicinList.route) {
            MedicinListScreen(
                navigateToDetail = { medicinId ->
                    navController.navigate(Screen.MedicinDetail.route.replace("{medicinId}", medicinId.toString()))
                },
                navigateToAddEdit = { medicinId ->
                    val route = Screen.AddEditMedicin.route.replace("{medicinId}", medicinId?.toString() ?: "")
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = Screen.MedicinDetail.route,
            arguments = listOf(navArgument("medicinId") { type = NavType.LongType })
        ) {
            val medicinId = it.arguments?.getLong("medicinId") ?: 0L
            MedicinDetailScreen(
                medicinId = medicinId,
                navigateUp = { navController.popBackStack() },
                navigateToEdit = { navController.navigate(Screen.AddEditMedicin.route.replace("{medicinId}", medicinId.toString())) },
                navigateToAddMovement = { lotId ->
                    navController.navigate(Screen.AddMovement.route.replace("{lotId}", lotId.toString()))
                },
                navigateToDetail = { id ->
                    navController.navigate(Screen.MedicinDetail.route.replace("{medicinId}", id.toString()))
                }
            )
        }

        composable(
            route = Screen.AddEditMedicin.route,
            arguments = listOf(navArgument("medicinId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            val medicinIdStr = it.arguments?.getString("medicinId")
            val medicinId = medicinIdStr?.toLongOrNull()
            AddEditMedicinScreen(
                medicinId = medicinId,
                navigateUp = { navController.popBackStack() }
            )
        }

        // Movement screens
        composable(Screen.MovementList.route) {
            MovementListScreen(
                navigateToAddMovement = { lotId ->
                    val route = if (lotId != null) {
                        Screen.AddMovement.route.replace("{lotId}", lotId.toString())
                    } else {
                        Screen.AddMovement.route.replace("?lotId={lotId}", "")
                    }
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = Screen.AddMovement.route,
            arguments = listOf(navArgument("lotId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            val lotIdStr = it.arguments?.getString("lotId")
            val lotId = lotIdStr?.toLongOrNull()
            AddMovementScreen(
                lotId = lotId,
                navigateUp = { navController.popBackStack() }
            )
        }

        // Alerts screen
        composable(Screen.Alerts.route) {
            AlertsScreen(
                navigateToMedicinDetail = { medicinId ->
                    navController.navigate(Screen.MedicinDetail.route.replace("{medicinId}", medicinId.toString()))
                }
            )
        }

        // Admin screen
        composable(Screen.Admin.route) {
            AdminScreen()
        }

        // Logs screen
        composable(Screen.Logs.route) {
            LogsScreen()
        }

        // Settings screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                navigateUp = { navController.popBackStack() }
            )
        }
    }
}