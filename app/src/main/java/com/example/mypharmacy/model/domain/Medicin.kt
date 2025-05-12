package com.example.mypharmacy.model.domain


data class Medicin(
    val id: Int = 0,
    val name: String,
    val description: String,
    val codeBarres: String? = null,
    val categorie: String? = null,
    val fabriquant: String,
    val seuilAlerte: Int? = null,
    val quantity: Int? = null,
    val userId: String
)