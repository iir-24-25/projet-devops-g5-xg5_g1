package com.example.mypharmacy.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mypharmacy.view.navigation.NavGraph
import com.example.mypharmacy.view.navigation.Screen
import com.example.mypharmacy.view.theme.MyPharmacyTheme
import com.example.mypharmacy.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPharmacyTheme {
                MyPharmacyApp()
            }
        }
    }
}

@Composable
fun MyPharmacyApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Determine start destination based on authentication state
    val startDestination = if (authState.isLoggedIn) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (authState.isLoggedIn) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Only show bottom navigation if the current screen is in bottom nav items
                val currentRoute = currentDestination?.route ?: ""
                val currentScreen = Screen.fromRoute(currentRoute)

                if (currentScreen.showInBottomNav) {
                    NavigationBar {
                        Screen.getBottomNavItems().forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (currentDestination?.hierarchy?.any { dest -> dest.route == screen.route } == true) {
                                            screen.selectedIcon
                                        } else {
                                            screen.unselectedIcon
                                        },
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(screen.title) },
                                selected = currentDestination?.hierarchy?.any { dest -> dest.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navController = navController,
                authViewModel = authViewModel,
                startDestination = startDestination
            )
        }
    }
}