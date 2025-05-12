package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.User

data class UserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val users: List<User> = emptyList(),
    val selectedUser: User? = null,
    val filteredRole: Role? = null,
    val searchQuery: String? = null
)