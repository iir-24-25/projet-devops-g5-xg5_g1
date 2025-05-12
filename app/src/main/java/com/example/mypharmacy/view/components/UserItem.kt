package com.example.mypharmacy.view.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.User

/**
 * Item card for displaying a user
 *
 * @param user The user to display
 * @param onClick Called when the card is clicked
 * @param onBlockClick Called when the block/unblock option is selected
 * @param isBlockEnabled Whether blocking functionality is enabled
 */
@Composable
fun UserItem(
    user: User,
    onClick: () -> Unit,
    onBlockClick: () -> Unit = {},
    isBlockEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    val (roleIcon, roleColor) = when (user.role) {
        Role.ADMINISTRATEUR -> Pair(
            Icons.Default.AdminPanelSettings,
            MaterialTheme.colorScheme.primary
        )
        Role.MEDICIN -> Pair(
            Icons.Default.LocalPharmacy,
            MaterialTheme.colorScheme.secondary
        )
    }

    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User avatar or icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(roleColor.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = roleColor.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = roleColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Role indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = roleIcon,
                        contentDescription = null,
                        tint = roleColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = user.role.name.lowercase().capitalize(),
                        style = MaterialTheme.typography.bodySmall,
                        color = roleColor
                    )
                }
            }

            // Options menu
            if (isBlockEnabled) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (user.token == "BLOCKED") "Débloquer l'utilisateur" else "Bloquer l'utilisateur"
                                )
                            },
                            onClick = {
                                onBlockClick()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (user.token == "BLOCKED") Icons.Default.CheckCircle else Icons.Default.Block,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact version of the user item for use in lists or summary views
 */
@Composable
fun UserCompactItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (roleIcon, roleColor) = when (user.role) {
        Role.ADMINISTRATEUR -> Pair(
            Icons.Default.AdminPanelSettings,
            MaterialTheme.colorScheme.primary
        )
        Role.MEDICIN -> Pair(
            Icons.Default.LocalPharmacy,
            MaterialTheme.colorScheme.secondary
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = roleColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = roleIcon,
                contentDescription = user.role.name,
                tint = roleColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}