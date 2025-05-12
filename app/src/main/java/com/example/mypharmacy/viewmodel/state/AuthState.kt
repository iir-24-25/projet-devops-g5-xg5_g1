package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.User

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false
)