package com.example.mypharmacy.model.remote.model


import java.time.LocalDate
import java.time.LocalDateTime

data class LotDto(
    val id: Long? = null,
    val numeroLot: String,
    val dateExpiration: LocalDate,
    val dateEntree: LocalDateTime,
    val quantite: Int,
    val medicinId: Long,
    val userId: String
)