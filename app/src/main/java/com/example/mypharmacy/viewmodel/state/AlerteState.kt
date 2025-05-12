package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.Alerte
import com.example.mypharmacy.model.domain.TypeAlert

data class AlerteState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val alertes: List<Alerte> = emptyList(),
    val selectedAlerte: Alerte? = null,
    val filteredType: TypeAlert? = null,
    val showOnlyActive: Boolean = true
)