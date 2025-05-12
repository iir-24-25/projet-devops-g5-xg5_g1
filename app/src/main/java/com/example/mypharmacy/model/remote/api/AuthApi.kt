package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.UserDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("api/register")
    suspend fun register(@Body user: UserDto): String

    @POST("api/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): String
}