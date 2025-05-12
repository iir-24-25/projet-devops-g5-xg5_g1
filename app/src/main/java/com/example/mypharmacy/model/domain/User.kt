package com.example.mypharmacy.model.domain


data class User(
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val email: String,
    val role: Role,
    val token: String? = null
)