package com.example.mypharmacy.model.domain


import java.time.LocalDateTime

data class MouvementStock(
    val id: Long = 0,
    val motif: String,
    val dateMouvement: LocalDateTime,
    val type: TypeMouvement,
    val lotId: Long,
    val utilisateurId: Long,
    val quantite: Int,
    val lot: Lot? = null,
    val utilisateur: User? = null
)