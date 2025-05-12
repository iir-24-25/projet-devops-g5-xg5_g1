package com.example.mypharmacy.model.domain


import java.time.LocalDateTime

data class Alerte(
    val id: Long = 0,
    val type: TypeAlert,
    val message: String,
    val estResolue: Boolean,
    val dateAlerte: LocalDateTime,
    val lotId: Long,
    val lot: Lot? = null
)