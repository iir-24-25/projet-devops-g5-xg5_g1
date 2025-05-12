package com.example.mypharmacy.model.remote.model


import com.example.mypharmacy.model.domain.TypeMouvement
import java.time.LocalDateTime

data class MouvementStockDto(
    val id: Long? = null,
    val motif: String,
    val dateMouvement: LocalDateTime,
    val type: TypeMouvement,
    val lotId: Long,
    val utilisateurId: Long,
    val quantite: Int
)