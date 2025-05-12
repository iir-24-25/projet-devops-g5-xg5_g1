package com.example.mypharmacy.model.remote.model


import com.example.mypharmacy.model.domain.TypeAlert
import java.time.LocalDateTime

data class AlerteDto(
    val id: Long? = null,
    val type: TypeAlert,
    val message: String,
    val estResolue: Boolean,
    val dateAlerte: LocalDateTime,
    val lotId: Long
)