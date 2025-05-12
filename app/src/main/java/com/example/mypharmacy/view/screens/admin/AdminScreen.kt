package com.example.mypharmacy.view.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mypharmacy.model.domain.User
import com.example.mypharmacy.viewmodel.AdminViewModel

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Panel",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Text(
                text = "Error: ${state.error}",
                color = MaterialTheme.colorScheme.error
            )
            Button(
                onClick = { viewModel.clearError() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Dismiss")
            }
        } else {
            LazyColumn {
                items(state.users) { user ->
                    UserItem(
                        user = user,
                        onUserSelected = { viewModel.selectUser(it) },
                        onBlockUser = { viewModel.blockUser(it.id.toString()) },
                        onUnblockUser = { viewModel.unblockUser(it.id.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserItem(
    user: User,
    onUserSelected: (User) -> Unit,
    onBlockUser: (User) -> Unit,
    onUnblockUser: (User) -> Unit
) {
    // Since we don't have a status field to determine if a user is blocked,
    // we'll need to adapt this. For now, let's assume a user is considered
    // "blocked" if their token is null, but you may need to adjust this
    // based on your actual business logic.
    val isBlocked = user.token == null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = user.username, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodySmall)

                // Display user status based on our assumption
                val status = if (isBlocked) "Blocked" else "Active"
                val statusColor = if (isBlocked)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary

                Text(
                    text = status,
                    color = statusColor
                )
            }

            Row {
                TextButton(onClick = { onUserSelected(user) }) {
                    Text("Details")
                }

                if (isBlocked) {
                    Button(onClick = { onUnblockUser(user) }) {
                        Text("Unblock")
                    }
                } else {
                    Button(onClick = { onBlockUser(user) }) {
                        Text("Block")
                    }
                }
            }
        }
    }
}