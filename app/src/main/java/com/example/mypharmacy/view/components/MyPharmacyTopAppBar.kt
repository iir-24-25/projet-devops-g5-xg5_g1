package com.example.mypharmacy.view.components


import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

/**
 * Standard top app bar for the application with customizable content.
 *
 * @param title The title displayed in the center of the top app bar
 * @param showBackButton Whether to show a back button at the start of the top app bar
 * @param onBackClicked Callback when the back button is clicked
 * @param actions Optional composable for the actions section (right side) of the top app bar
 * @param showMenu Whether to show a dropdown menu
 * @param menuItems List of menu items to display in the dropdown menu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPharmacyTopAppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClicked: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    showMenu: Boolean = false,
    menuItems: List<MenuItem> = emptyList()
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            }
        },
        actions = {
            actions()

            if (showMenu && menuItems.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Plus d'options"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { menuItem ->
                        DropdownMenuItem(
                            text = { Text(menuItem.title) },
                            onClick = {
                                menuItem.onClick()
                                expanded = false
                            },
                            leadingIcon = menuItem.icon?.let {
                                { Icon(imageVector = it, contentDescription = null) }
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

/**
 * Data class representing a menu item in the top app bar dropdown menu
 *
 * @param title The title of the menu item
 * @param icon Optional icon for the menu item
 * @param onClick Callback when the menu item is clicked
 */
data class MenuItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val onClick: () -> Unit
)