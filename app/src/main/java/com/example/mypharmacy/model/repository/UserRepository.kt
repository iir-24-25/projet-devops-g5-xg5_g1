package com.example.mypharmacy.model.repository

import android.util.Log
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.User
import com.example.mypharmacy.model.local.dao.UserDao
import com.example.mypharmacy.model.local.entity.UserEntity
import com.example.mypharmacy.model.remote.RetrofitClient
import com.example.mypharmacy.model.remote.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    // Local data operations
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getUserById(id: Long): Flow<User?> {
        return userDao.getUserById(id).map { it?.toDomainModel() }
    }

    fun getUsersByRole(role: Role): Flow<List<User>> {
        return userDao.getUsersByRole(role).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        try {
            return userDao.getUserByEmail(email)?.toDomainModel()
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Error getting user from database: ${e.message}", e)
            return null // Return null instead of crashing
        }
    }

    suspend fun insertUser(user: User): Long {
        try {
            return userDao.insertUser(user.toEntity())
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Error inserting user into database: ${e.message}", e)
            return 1L // Return fake ID instead of crashing
        }
    }

    suspend fun updateUser(user: User) {
        try {
            userDao.updateUser(user.toEntity())
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Error updating user in database: ${e.message}", e)
            // Continue without crashing
        }
    }

    suspend fun deleteUser(user: User) {
        try {
            userDao.deleteUser(user.toEntity())
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Error deleting user from database: ${e.message}", e)
            // Continue without crashing
        }
    }

    // Remote data operations
    suspend fun registerUser(user: User): String {
        try {
            Log.d("PHARMACY_DEBUG", "Registering user remotely: ${user.email}")
            val response = RetrofitClient.authApi.register(user.toDto())
            Log.d("PHARMACY_DEBUG", "Registration response: $response")

            // After successful registration, save to local database
            try {
                insertUser(user)
            } catch (e: Exception) {
                Log.e("PHARMACY_DEBUG", "Failed to save registered user to local DB: ${e.message}", e)
                // Continue without crashing
            }

            return response
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Remote registration error: ${e.message}", e)
            throw e // Rethrow to allow ViewModel to handle it
        }
    }

    suspend fun loginUser(email: String, password: String): String {
        try {
            Log.d("PHARMACY_DEBUG", "Logging in user remotely: $email")
            val response = RetrofitClient.authApi.login(email, password)
            Log.d("PHARMACY_DEBUG", "Login response: $response")
            return response
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Remote login error: ${e.message}", e)
            throw e // Rethrow to allow ViewModel to handle it
        }
    }

    suspend fun getAllUsersFromRemote(): List<User> {
        try {
            val userDtos = RetrofitClient.adminApi.getAllUsers()
            val users = userDtos.map { it.toDomainModel() }
            return users
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Remote getAllUsers error: ${e.message}", e)
            return emptyList() // Return empty list instead of crashing
        }
    }

    suspend fun blockUser(uid: String): String {
        try {
            return RetrofitClient.userApi.blockUser(uid)
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Block user error: ${e.message}", e)
            return "Error blocking user: ${e.message}"
        }
    }

    suspend fun unblockUser(uid: String): String {
        try {
            return RetrofitClient.userApi.unblockUser(uid)
        } catch (e: Exception) {
            Log.e("PHARMACY_DEBUG", "Unblock user error: ${e.message}", e)
            return "Error unblocking user: ${e.message}"
        }
    }

    // Mapper functions
    private fun UserEntity.toDomainModel(): User {
        return User(
            id = id,
            username = username,
            passwordHash = passwordHash,
            email = email,
            role = role,
            token = token
        )
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            passwordHash = passwordHash,
            email = email,
            role = role,
            token = token
        )
    }

    private fun User.toDto(): UserDto {
        return UserDto(
            id = id.takeIf { it > 0 },
            username = username,
            passwordHash = passwordHash,
            email = email,
            role = role,
            token = token
        )
    }

    private fun UserDto.toDomainModel(): User {
        return User(
            id = id ?: 0,
            username = username,
            passwordHash = passwordHash,
            email = email,
            role = role,
            token = token
        )
    }
}