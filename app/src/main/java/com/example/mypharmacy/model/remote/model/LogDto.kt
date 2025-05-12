package com.example.mypharmacy.model.remote.model


import java.time.LocalDateTime

data class LogDto(
    val id: Long? = null,
    val action: String,
    val dateAction: LocalDateTime,
    val utilisateurId: Long
)