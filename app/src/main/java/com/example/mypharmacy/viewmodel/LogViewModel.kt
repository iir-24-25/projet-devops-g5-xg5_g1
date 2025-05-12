package com.example.mypharmacy.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Log
import com.example.mypharmacy.model.repository.LogRepository
import com.example.mypharmacy.viewmodel.state.LogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logRepository: LogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LogState())
    val state: StateFlow<LogState> = _state.asStateFlow()

    init {
        loadAllLogs()
    }

    fun loadAllLogs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                logRepository.getAllLogs().collectLatest { logs ->
                    _state.update {
                        it.copy(
                            logs = logs,
                            isLoading = false,
                            startDate = null,
                            endDate = null,
                            searchQuery = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLogsByUserId(userId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                logRepository.getLogsByUserId(userId).collectLatest { logs ->
                    _state.update { it.copy(logs = logs, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadLogsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
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
                logRepository.getLogsByDateRange(startDate, endDate).collectLatest { logs ->
                    _state.update { it.copy(logs = logs, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun searchLogs(query: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    searchQuery = query
                )
            }
            try {
                logRepository.searchLogs(query).collectLatest { logs ->
                    _state.update { it.copy(logs = logs, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectLog(log: Log) {
        _state.update { it.copy(selectedLog = log) }
    }

    fun createLog(log: Log) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                logRepository.createLog(log)
                loadAllLogs()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateLog(log: Log) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                logRepository.updateLog(log)
                loadAllLogs()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteLog(log: Log) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                logRepository.deleteLog(log)
                loadAllLogs()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun logAction(utilisateurId: Long, action: String) {
        viewModelScope.launch {
            try {
                logRepository.logAction(utilisateurId, action)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearFilters() {
        _state.update {
            it.copy(
                startDate = null,
                endDate = null,
                searchQuery = null
            )
        }
        loadAllLogs()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}