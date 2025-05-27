package com.example.mypharmacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Lot
import com.example.mypharmacy.model.repository.LotRepository
import com.example.mypharmacy.viewmodel.state.LotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotViewModel @Inject constructor(
    private val lotRepository: LotRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LotState())
    val state: StateFlow<LotState> = _state.asStateFlow()

    init {
        loadAllLots()
    }

    fun loadAllLots() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.getAllLots().collectLatest { lots ->
                    _state.update {
                        it.copy(
                            lots = lots,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLotById(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.getLotById(id).collectLatest { lot ->
                    _state.update { it.copy(selectedLot = lot, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLotsByMedicinId(medicinId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.getLotsByMedicinId(medicinId).collectLatest { lots ->
                    _state.update { it.copy(lots = lots, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun createLot(lot: Lot) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.insertLot(lot)
                loadAllLots()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateLot(lot: Lot) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.updateLot(lot)
                loadAllLots()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteLot(lot: Lot) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                lotRepository.deleteLot(lot)
                loadAllLots()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}