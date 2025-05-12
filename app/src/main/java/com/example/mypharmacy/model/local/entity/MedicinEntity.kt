package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicins")
data class MedicinEntity(
    @PrimaryKey(autoGenerate = true)
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