package com.example.mypharmacy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.model.repository.AlerteRepository
import com.example.mypharmacy.model.repository.MedicinRepository
import com.example.mypharmacy.viewmodel.state.MedicinState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicinViewModel @Inject constructor(
    private val medicinRepository: MedicinRepository,
    private val alerteRepository: AlerteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MedicinState())
    val state: StateFlow<MedicinState> = _state.asStateFlow()

    init {
        loadAllMedicins()
    }

    fun loadAllMedicins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, showOnlyLowStock = false) }
            try {
                medicinRepository.getAllMedicins().collectLatest { medicins ->
                    val filteredMedicins = applyFilters(medicins)
                    _state.update { it.copy(medicins = filteredMedicins, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadMedicinsByUserId(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                medicinRepository.getMedicinsByUserId(userId).collectLatest { medicins ->
                    val filteredMedicins = applyFilters(medicins)
                    _state.update { it.copy(medicins = filteredMedicins, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLowStockMedicins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, showOnlyLowStock = true) }
            try {
                medicinRepository.getLowStockMedicins().collectLatest { medicins ->
                    val filteredMedicins = applyFilters(medicins)
                    _state.update { it.copy(medicins = filteredMedicins, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLowStockMedicinsByUserId(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, showOnlyLowStock = true) }
            try {
                medicinRepository.getLowStockMedicinsByUserId(userId).collectLatest { medicins ->
                    val filteredMedicins = applyFilters(medicins)
                    _state.update { it.copy(medicins = filteredMedicins, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectMedicin(medicin: Medicin) {
        _state.update { it.copy(selectedMedicin = medicin) }
    }

    fun createMedicin(medicin: Medicin) {
        viewModelScope.launch {
            Log.d("MedicinViewModel", "Création de médicament: ${medicin.name}")
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // 1. Ajouter d'abord à la base de données locale pour affichage immédiat
                val localId = medicinRepository.insertMedicin(medicin)

                // 2. Créer une copie avec l'ID local pour l'affichage
                val localMedicin = medicin.copy(id = localId.toInt())

                // 3. Mettre à jour l'UI immédiatement avec la version locale
                _state.update { currentState ->
                    val updatedMedicins = currentState.medicins.toMutableList()
                    updatedMedicins.add(localMedicin)
                    // Et aussi sélectionner le médicament pour l'écran de détail
                    currentState.copy(
                        medicins = updatedMedicins,
                        selectedMedicin = localMedicin,
                        isLoading = false
                    )
                }

                Log.d("MedicinViewModel", "Médicament ajouté à l'UI, ID: ${localMedicin.id}")

                // 4. Vérifier si le stock est bas et créer une alerte si nécessaire
                if (medicin.quantity != null && medicin.seuilAlerte != null &&
                    medicin.quantity <= medicin.seuilAlerte) {
                    alerteRepository.checkForLowStockAlerts(
                        medicin = localId.toInt().toLong(),
                        quantity = medicin.quantity,
                        seuilAlerte = medicin.seuilAlerte,
                        lotId = 0 // Valeur par défaut
                    )
                }

                // 5. Essayer d'envoyer au serveur en arrière-plan
                viewModelScope.launch {
                    try {
                        Log.d("MedicinViewModel", "Envoi au serveur: ${medicin.name}")
                        val remoteMedicin = medicinRepository.createMedicin(medicin)
                        Log.d("MedicinViewModel", "Réponse du serveur, ID: ${remoteMedicin.id}")

                        // Rafraîchir pour synchroniser la version distante
                        refreshMedicins()
                    } catch (e: Exception) {
                        Log.e("MedicinViewModel", "Erreur serveur: ${e.message}", e)
                        // Ne pas modifier l'UI ici - le médicament est déjà affiché localement
                    }
                }
            } catch (e: Exception) {
                Log.e("MedicinViewModel", "Erreur locale: ${e.message}", e)
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateMedicin(medicin: Medicin) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Mettre à jour localement pour affichage immédiat
                medicinRepository.updateMedicin(medicin)

                // Mettre à jour l'UI immédiatement
                _state.update { currentState ->
                    val updatedMedicins = currentState.medicins.map {
                        if (it.id == medicin.id) medicin else it
                    }
                    // Mettre à jour également le médicament sélectionné si c'est celui qui est modifié
                    val updatedSelectedMedicin = if (currentState.selectedMedicin?.id == medicin.id) {
                        medicin
                    } else {
                        currentState.selectedMedicin
                    }

                    currentState.copy(
                        medicins = updatedMedicins,
                        selectedMedicin = updatedSelectedMedicin,
                        isLoading = false
                    )
                }

                // Vérifier le stock
                if (medicin.quantity != null && medicin.seuilAlerte != null &&
                    medicin.quantity <= medicin.seuilAlerte) {
                    alerteRepository.checkForLowStockAlerts(
                        medicin = medicin.id.toLong(),
                        quantity = medicin.quantity,
                        seuilAlerte = medicin.seuilAlerte,
                        lotId = 0
                    )
                }

                // Mettre à jour sur le serveur en arrière-plan
                viewModelScope.launch {
                    try {
                        medicinRepository.updateMedicinRemote(medicin.id.toLong(), medicin)
                        refreshMedicins()
                    } catch (e: Exception) {
                        Log.e("MedicinViewModel", "Erreur mise à jour serveur: ${e.message}", e)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteMedicin(medicin: Medicin) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Supprimer localement
                medicinRepository.deleteMedicin(medicin)

                // Mettre à jour l'UI immédiatement
                _state.update { currentState ->
                    val updatedMedicins = currentState.medicins.filter { it.id != medicin.id }

                    // Si le médicament supprimé est celui sélectionné, désélectionner
                    val updatedSelectedMedicin = if (currentState.selectedMedicin?.id == medicin.id) {
                        null
                    } else {
                        currentState.selectedMedicin
                    }

                    currentState.copy(
                        medicins = updatedMedicins,
                        selectedMedicin = updatedSelectedMedicin,
                        isLoading = false
                    )
                }

                // Supprimer sur le serveur en arrière-plan
                viewModelScope.launch {
                    try {
                        medicinRepository.deleteMedicinRemote(medicin.id.toLong())
                        refreshMedicins()
                    } catch (e: Exception) {
                        Log.e("MedicinViewModel", "Erreur suppression serveur: ${e.message}", e)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun syncWithRemote(userId: String? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val remoteMedicins = medicinRepository.getMedicinsRemote(userId)
                refreshMedicins()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setSearchQuery(query: String?) {
        _state.update { it.copy(searchQuery = query) }
        refreshMedicins()
    }

    fun setCategoryFilter(category: String?) {
        _state.update { it.copy(categoryFilter = category) }
        refreshMedicins()
    }

    fun setLowStockFilter(showOnlyLowStock: Boolean) {
        _state.update { it.copy(showOnlyLowStock = showOnlyLowStock) }
        refreshMedicins()
    }

    fun clearFilters() {
        _state.update {
            it.copy(
                searchQuery = null,
                categoryFilter = null,
                showOnlyLowStock = false
            )
        }
        loadAllMedicins()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun refreshMedicins() {
        if (_state.value.showOnlyLowStock) {
            loadLowStockMedicins()
        } else {
            loadAllMedicins()
        }
    }

    private fun applyFilters(medicins: List<Medicin>): List<Medicin> {
        var result = medicins

        // Apply search query if any
        _state.value.searchQuery?.let { query ->
            if (query.isNotEmpty()) {
                result = result.filter { medicin ->
                    medicin.name.contains(query, ignoreCase = true) ||
                            medicin.description.contains(query, ignoreCase = true) ||
                            medicin.fabriquant.contains(query, ignoreCase = true) ||
                            medicin.categorie?.contains(query, ignoreCase = true) == true
                }
            }
        }

        // Apply category filter if any
        _state.value.categoryFilter?.let { category ->
            if (category.isNotEmpty()) {
                result = result.filter { it.categorie == category }
            }
        }

        return result
    }
}