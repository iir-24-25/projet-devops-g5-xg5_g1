package com.example.mypharmacy.model.domain


import java.time.LocalDateTime

data class Log(
    val id: Long = 0,
    val action: String,
    val dateAction: LocalDateTime,
    val utilisateurId: Long,
    val utilisateur: User? = null
)