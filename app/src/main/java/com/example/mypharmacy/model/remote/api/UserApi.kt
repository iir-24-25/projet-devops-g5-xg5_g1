package com.example.mypharmacy.model.remote.api


import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("firebase/block/{uid}")
    suspend fun blockUser(@Path("uid") uid: String): String

    @POST("firebase/unblock/{uid}")
    suspend fun unblockUser(@Path("uid") uid: String): String
}