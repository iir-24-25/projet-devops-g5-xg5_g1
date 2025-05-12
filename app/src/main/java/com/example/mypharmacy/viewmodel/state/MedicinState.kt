package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.Medicin

data class MedicinState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val medicins: List<Medicin> = emptyList(),
    val selectedMedicin: Medicin? = null,
    val showOnlyLowStock: Boolean = false,
    val searchQuery: String? = null,
    val categoryFilter: String? = null
)