package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.model.domain.TypeMouvement
import java.time.LocalDateTime

data class MouvementStockState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mouvements: List<MouvementStock> = emptyList(),
    val selectedMouvement: MouvementStock? = null,
    val filteredType: TypeMouvement? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val medicinId: Long? = null,
    val lotId: Long? = null
)