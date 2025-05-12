package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.UserDto
import retrofit2.http.GET

interface AdminApi {
    @GET("admin/all-users")
    suspend fun getAllUsers(): List<UserDto>
}