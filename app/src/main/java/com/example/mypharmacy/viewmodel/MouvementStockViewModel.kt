package com.example.mypharmacy.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.model.repository.LogRepository
import com.example.mypharmacy.model.repository.MedicinRepository
import com.example.mypharmacy.model.repository.MouvementStockRepository
import com.example.mypharmacy.viewmodel.state.MouvementStockState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MouvementStockViewModel @Inject constructor(
    private val mouvementStockRepository: MouvementStockRepository,
    private val medicinRepository: MedicinRepository,
    private val logRepository: LogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MouvementStockState())
    val state: StateFlow<MouvementStockState> = _state.asStateFlow()

    init {
        loadAllMouvements()
    }

    fun loadAllMouvements() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                mouvementStockRepository.getAllMouvements().collectLatest { mouvements ->
                    _state.update {
                        it.copy(
                            mouvements = mouvements,
                            isLoading = false,
                            filteredType = null,
                            startDate = null,
                            endDate = null,
                            medicinId = null,
                            lotId = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByLotId(lotId: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    lotId = lotId
                )
            }
            try {
                mouvementStockRepository.getMouvementsByLotId(lotId).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByUserId(userId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                mouvementStockRepository.getMouvementsByUserId(userId).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByLotUserId(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                mouvementStockRepository.getMouvementsByLotUserId(userId).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByType(type: TypeMouvement) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    filteredType = type
                )
            }
            try {
                mouvementStockRepository.getMouvementsByType(type).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    startDate = startDate,
                    endDate = endDate
                )
            }
            try {
                mouvementStockRepository.getMouvementsByDateRange(startDate, endDate).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMouvementsByMedicinId(medicinId: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    medicinId = medicinId
                )
            }
            try {
                mouvementStockRepository.getMouvementsByMedicinId(medicinId).collectLatest { mouvements ->
                    _state.update { it.copy(mouvements = mouvements, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectMouvement(mouvement: MouvementStock) {
        _state.update { it.copy(selectedMouvement = mouvement) }
    }

    fun createMouvement(mouvement: MouvementStock) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                mouvementStockRepository.insertMouvement(mouvement)

                // Update medicine quantity based on movement type
                val medicinFlow = medicinRepository.getMedicinById(mouvement.lotId)
                val medicin = medicinFlow.firstOrNull()
                medicin?.let {
                    val updatedQuantity = when (mouvement.type) {
                        TypeMouvement.ENTREE -> (it.quantity ?: 0) + mouvement.quantite
                        TypeMouvement.SORTIE -> (it.quantity ?: 0) - mouvement.quantite
                    }
                    val updatedMedicin = it.copy(quantity = updatedQuantity)
                    medicinRepository.updateMedicin(updatedMedicin)
                }

                // Log the action
                val actionType = if (mouvement.type == TypeMouvement.ENTREE) "entrée" else "sortie"
                logRepository.logAction(
                    mouvement.utilisateurId,
                    "Mouvement de stock: $actionType de ${mouvement.quantite} unités, motif: ${mouvement.motif}"
                )

                loadAllMouvements()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateMouvement(mouvement: MouvementStock) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Get original movement to calculate quantity difference
                val originalMouvementFlow = mouvementStockRepository.getMouvementById(mouvement.id)
                val originalMouvement = originalMouvementFlow.firstOrNull()

                mouvementStockRepository.updateMouvement(mouvement)

                // Update medicine quantity based on the difference
                originalMouvement?.let {
                    val medicinFlow = medicinRepository.getMedicinById(mouvement.lotId)
                    val medicin = medicinFlow.firstOrNull()
                    medicin?.let { med ->
                        val quantityDifference = mouvement.quantite - it.quantite
                        val updatedQuantity = when (mouvement.type) {
                            TypeMouvement.ENTREE -> (med.quantity ?: 0) + quantityDifference
                            TypeMouvement.SORTIE -> (med.quantity ?: 0) - quantityDifference
                        }
                        val updatedMedicin = med.copy(quantity = updatedQuantity)
                        medicinRepository.updateMedicin(updatedMedicin)
                    }
                }

                // Log the action
                logRepository.logAction(
                    mouvement.utilisateurId,
                    "Modification d'un mouvement de stock, ID: ${mouvement.id}"
                )

                loadAllMouvements()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteMouvement(mouvement: MouvementStock) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Revert medicine quantity change
                val medicinFlow = medicinRepository.getMedicinById(mouvement.lotId)
                val medicin = medicinFlow.firstOrNull()
                medicin?.let {
                    val updatedQuantity = when (mouvement.type) {
                        TypeMouvement.ENTREE -> (it.quantity ?: 0) - mouvement.quantite
                        TypeMouvement.SORTIE -> (it.quantity ?: 0) + mouvement.quantite
                    }
                    val updatedMedicin = it.copy(quantity = updatedQuantity)
                    medicinRepository.updateMedicin(updatedMedicin)
                }

                mouvementStockRepository.deleteMouvement(mouvement)

                // Log the action
                logRepository.logAction(
                    mouvement.utilisateurId,
                    "Suppression d'un mouvement de stock, ID: ${mouvement.id}"
                )

                loadAllMouvements()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun syncWithRemote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val remoteMouvements = mouvementStockRepository.getAllMouvementsRemote()
                loadAllMouvements()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearFilters() {
        _state.update {
            it.copy(
                filteredType = null,
                startDate = null,
                endDate = null,
                medicinId = null,
                lotId = null
            )
        }
        loadAllMouvements()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}