package com.example.mypharmacy.model.remote.model


import com.example.mypharmacy.model.domain.Role

data class UserDto(
    val id: Long? = null,
    val username: String,
    val passwordHash: String,
    val email: String,
    val role: Role,
    val token: String? = null
)