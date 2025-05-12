package com.example.mypharmacy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.User
import com.example.mypharmacy.model.repository.UserRepository
import com.example.mypharmacy.viewmodel.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.d("PHARMACY_DEBUG", "Starting login for: $email")
                _state.update { it.copy(isLoading = true, error = null, loginSuccess = false) }

                // First check if user exists in local database
                val localUser = userRepository.getUserByEmail(email)
                Log.d("PHARMACY_DEBUG", "Local user found: ${localUser != null}")

                if (localUser != null && localUser.passwordHash == password) {
                    Log.d("PHARMACY_DEBUG", "Local login successful")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            currentUser = localUser,
                            loginSuccess = true
                        )
                    }
                    return@launch
                }

                // If not found locally, try remote login
                Log.d("PHARMACY_DEBUG", "Attempting remote login")
                try {
                    val response = userRepository.loginUser(email, password)
                    Log.d("PHARMACY_DEBUG", "Remote login response: $response")

                    if (response.contains("Connecté en tant que")) {
                        // Extract role from response
                        val roleStr = response.substringAfter("Connecté en tant que").trim()
                        val role = try {
                            Role.valueOf(roleStr)
                        } catch (e: Exception) {
                            Log.e("PHARMACY_DEBUG", "Role parsing error: ${e.message}", e)
                            Role.MEDICIN // Default role if parsing fails
                        }

                        // Create user object
                        val user = User(
                            username = email.substringBefore("@"),
                            email = email,
                            passwordHash = password,
                            role = role
                        )

                        // Save user to local database
                        try {
                            userRepository.insertUser(user)
                            Log.d("PHARMACY_DEBUG", "User saved to local DB after remote login")
                        } catch (e: Exception) {
                            Log.e("PHARMACY_DEBUG", "Failed to save user to local DB: ${e.message}", e)
                            // Continue without crashing
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                currentUser = user,
                                loginSuccess = true
                            )
                        }
                        Log.d("PHARMACY_DEBUG", "Remote login successful")
                    } else {
                        Log.d("PHARMACY_DEBUG", "Invalid credentials response")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Échec de connexion: Identifiants invalides"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PHARMACY_DEBUG", "Remote login exception: ${e.message}", e)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Erreur de connexion: ${e.message ?: "Erreur inconnue"}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("PHARMACY_DEBUG", "Login process crashed: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur: ${e.message ?: "Erreur inconnue"}"
                    )
                }
            }
        }
    }

    fun register(username: String, email: String, password: String, role: Role = Role.MEDICIN) {
        viewModelScope.launch {
            try {
                Log.d("PHARMACY_DEBUG", "Starting registration for: $email")
                _state.update { it.copy(isLoading = true, error = null, registerSuccess = false) }

                // Check if user already exists in local database
                val existingUser = userRepository.getUserByEmail(email)
                Log.d("PHARMACY_DEBUG", "Existing local user: ${existingUser != null}")

                if (existingUser != null) {
                    Log.d("PHARMACY_DEBUG", "User already exists locally")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Un utilisateur avec cet email existe déjà"
                        )
                    }
                    return@launch
                }

                // Create new user
                val newUser = User(
                    username = username,
                    email = email,
                    passwordHash = password,
                    role = role
                )

                // Try to register remotely
                try {
                    Log.d("PHARMACY_DEBUG", "Attempting remote registration")
                    val response = userRepository.registerUser(newUser)
                    Log.d("PHARMACY_DEBUG", "Remote registration response: $response")

                    if (response.contains("créé")) {
                        Log.d("PHARMACY_DEBUG", "Remote registration successful")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                registerSuccess = true
                            )
                        }
                    } else {
                        // If remote fails but seems ok, still save locally
                        Log.d("PHARMACY_DEBUG", "Remote response unclear, saving locally")
                        try {
                            userRepository.insertUser(newUser)
                        } catch (e: Exception) {
                            Log.e("PHARMACY_DEBUG", "Failed to save user locally: ${e.message}", e)
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                registerSuccess = true
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PHARMACY_DEBUG", "Remote registration error: ${e.message}", e)

                    // If remote fails, save locally at least
                    try {
                        userRepository.insertUser(newUser)
                        Log.d("PHARMACY_DEBUG", "User saved locally despite remote error")
                    } catch (dbEx: Exception) {
                        Log.e("PHARMACY_DEBUG", "Failed to save user locally: ${dbEx.message}", dbEx)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            registerSuccess = true,
                            error = "Sauvegardé localement uniquement: ${e.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("PHARMACY_DEBUG", "Registration process crashed: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur: ${e.message ?: "Erreur inconnue"}"
                    )
                }
            }
        }
    }

    fun logout() {
        _state.update {
            AuthState(
                isLoggedIn = false,
                currentUser = null
            )
        }
    }

    fun resetLoginState() {
        _state.update { it.copy(loginSuccess = false, error = null) }
    }

    fun resetRegisterState() {
        _state.update { it.copy(registerSuccess = false, error = null) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}