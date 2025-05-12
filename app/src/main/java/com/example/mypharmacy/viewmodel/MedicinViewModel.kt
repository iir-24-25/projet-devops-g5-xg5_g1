package com.example.mypharmacy.viewmodel


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
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val id = medicinRepository.insertMedicin(medicin)

                // Check if stock is low and create alert if needed
                if (medicin.quantity != null && medicin.seuilAlerte != null &&
                    medicin.quantity <= medicin.seuilAlerte) {
                    alerteRepository.checkForLowStockAlerts(
                        medicin = id.toInt().toLong(),
                        quantity = medicin.quantity,
                        seuilAlerte = medicin.seuilAlerte,
                        lotId = 0 // Default value, should be updated with actual lot
                    )
                }

                refreshMedicins()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateMedicin(medicin: Medicin) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                medicinRepository.updateMedicin(medicin)

                // Check if stock is low and create alert if needed
                if (medicin.quantity != null && medicin.seuilAlerte != null &&
                    medicin.quantity <= medicin.seuilAlerte) {
                    alerteRepository.checkForLowStockAlerts(
                        medicin = medicin.id.toLong(),
                        quantity = medicin.quantity,
                        seuilAlerte = medicin.seuilAlerte,
                        lotId = 0 // Default value, should be updated with actual lot
                    )
                }

                refreshMedicins()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteMedicin(medicin: Medicin) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                medicinRepository.deleteMedicin(medicin)
                refreshMedicins()
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