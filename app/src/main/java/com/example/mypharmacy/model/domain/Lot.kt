package com.example.mypharmacy.model.domain


import java.time.LocalDate
import java.time.LocalDateTime

data class Lot(
    val id: Long = 0,
    val numeroLot: String,
    val dateExpiration: LocalDate,
    val dateEntree: LocalDateTime,
    val quantite: Int,
    val medicinId: Long,
    val userId: String,
    val medicin: Medicin? = null
)