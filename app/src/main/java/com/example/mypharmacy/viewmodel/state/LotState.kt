package com.example.mypharmacy.viewmodel.state

import com.example.mypharmacy.model.domain.Lot

data class LotState(
    val lots: List<Lot> = emptyList(),
    val selectedLot: Lot? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)