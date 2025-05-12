package com.example.mypharmacy.model.remote.model


data class MedicinDto(
    val id: Int? = null,
    val name: String,
    val description: String,
    val codeBarres: String? = null,
    val categorie: String? = null,
    val fabriquant: String,
    val seuilAlerte: Int? = null,
    val quantity: Int? = null,
    val userId: String
)