package com.example.mypharmacy.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.User
import com.example.mypharmacy.model.repository.UserRepository
import com.example.mypharmacy.viewmodel.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        loadAllUsers()
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.getAllUsers().collectLatest { users ->
                    val filteredUsers = applyFilters(users)
                    _state.update { it.copy(users = filteredUsers, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadUsersByRole(role: Role) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, filteredRole = role) }
            try {
                userRepository.getUsersByRole(role).collectLatest { users ->
                    val filteredUsers = applySearchFilter(users)
                    _state.update { it.copy(users = filteredUsers, isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun getUserById(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.getUserById(id).collectLatest { user ->
                    user?.let {
                        _state.update { state -> state.copy(selectedUser = it, isLoading = false) }
                    } ?: _state.update { state ->
                        state.copy(
                            error = "Utilisateur non trouvé",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun selectUser(user: User) {
        _state.update { it.copy(selectedUser = user) }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.insertUser(user)
                loadAllUsers()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.updateUser(user)
                loadAllUsers()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                userRepository.deleteUser(user)
                loadAllUsers()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun blockUser(uid: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = userRepository.blockUser(uid)
                loadAllUsers() // Reload after blocking
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun unblockUser(uid: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = userRepository.unblockUser(uid)
                loadAllUsers() // Reload after unblocking
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun setSearchQuery(query: String?) {
        _state.update { it.copy(searchQuery = query) }
        refreshUsers()
    }

    fun setRoleFilter(role: Role?) {
        _state.update { it.copy(filteredRole = role) }
        refreshUsers()
    }

    fun clearFilters() {
        _state.update {
            it.copy(
                searchQuery = null,
                filteredRole = null
            )
        }
        loadAllUsers()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun refreshUsers() {
        val role = _state.value.filteredRole
        if (role != null) {
            loadUsersByRole(role)
        } else {
            loadAllUsers()
        }
    }

    private fun applyFilters(users: List<User>): List<User> {
        var result = users

        // Apply role filter if any
        _state.value.filteredRole?.let { role ->
            result = result.filter { it.role == role }
        }

        // Apply search filter
        result = applySearchFilter(result)

        return result
    }

    private fun applySearchFilter(users: List<User>): List<User> {
        var result = users

        // Apply search query if any
        _state.value.searchQuery?.let { query ->
            if (query.isNotEmpty()) {
                result = result.filter { user ->
                    user.username.contains(query, ignoreCase = true) ||
                            user.email.contains(query, ignoreCase = true)
                }
            }
        }

        return result
    }
}