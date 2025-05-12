package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.User

data class AdminState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val users: List<User> = emptyList(),
    val selectedUser: User? = null
)