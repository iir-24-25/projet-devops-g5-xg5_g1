package com.example.mypharmacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.User
import com.example.mypharmacy.model.repository.UserRepository
import com.example.mypharmacy.viewmodel.state.AdminState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state.asStateFlow()

    init {
        loadAllUsers()
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.getAllUsers().collectLatest { users ->
                    _state.update { it.copy(users = users, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadRemoteUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val users = userRepository.getAllUsersFromRemote()
                _state.update { it.copy(users = users, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectUser(user: User) {
        _state.update { it.copy(selectedUser = user) }
    }

    fun blockUser(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.blockUser(userId)
                loadAllUsers() // Reload after action
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun unblockUser(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.unblockUser(userId)
                loadAllUsers() // Reload after action
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}