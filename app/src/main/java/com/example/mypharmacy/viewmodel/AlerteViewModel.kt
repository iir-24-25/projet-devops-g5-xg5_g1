package com.example.mypharmacy.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Alerte
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.repository.AlerteRepository
import com.example.mypharmacy.viewmodel.state.AlerteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlerteViewModel @Inject constructor(
    private val alerteRepository: AlerteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlerteState())
    val state: StateFlow<AlerteState> = _state.asStateFlow()

    init {
        loadActiveAlertes()
    }

    fun loadAllAlertes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, showOnlyActive = false) }
            try {
                alerteRepository.getAllAlertes().collectLatest { alertes ->
                    _state.update { it.copy(alertes = alertes, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadActiveAlertes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, showOnlyActive = true) }
            try {
                alerteRepository.getActiveAlertes().collectLatest { alertes ->
                    _state.update { it.copy(alertes = alertes, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadAlertesByUserId(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                if (_state.value.showOnlyActive) {
                    alerteRepository.getActiveAlertesByUserId(userId).collectLatest { alertes ->
                        _state.update { it.copy(alertes = alertes, isLoading = false) }
                    }
                } else {
                    alerteRepository.getAlertesByUserId(userId).collectLatest { alertes ->
                        _state.update { it.copy(alertes = alertes, isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadAlertesByType(type: TypeAlert) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, filteredType = type) }
            try {
                alerteRepository.getAlertesByType(type).collectLatest { alertes ->
                    val filteredAlertes = if (_state.value.showOnlyActive) {
                        alertes.filter { !it.estResolue }
                    } else {
                        alertes
                    }
                    _state.update { it.copy(alertes = filteredAlertes, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectAlerte(alerte: Alerte) {
        _state.update { it.copy(selectedAlerte = alerte) }
    }

    fun createAlerte(alerte: Alerte) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                alerteRepository.createAlerte(alerte)
                if (_state.value.showOnlyActive) {
                    loadActiveAlertes()
                } else {
                    loadAllAlertes()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateAlerte(alerte: Alerte) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                alerteRepository.updateAlerte(alerte)
                if (_state.value.showOnlyActive) {
                    loadActiveAlertes()
                } else {
                    loadAllAlertes()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteAlerte(alerte: Alerte) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                alerteRepository.deleteAlerte(alerte)
                if (_state.value.showOnlyActive) {
                    loadActiveAlertes()
                } else {
                    loadAllAlertes()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun markAlerteAsResolved(alerteId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                alerteRepository.markAlerteAsResolved(alerteId)
                if (_state.value.showOnlyActive) {
                    loadActiveAlertes()
                } else {
                    loadAllAlertes()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setShowOnlyActive(showOnlyActive: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(showOnlyActive = showOnlyActive) }
            if (showOnlyActive) {
                loadActiveAlertes()
            } else {
                loadAllAlertes()
            }
        }
    }

    fun clearFilter() {
        _state.update { it.copy(filteredType = null) }
        if (_state.value.showOnlyActive) {
            loadActiveAlertes()
        } else {
            loadAllAlertes()
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}