package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.AlerteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlerteApi {
    @POST("alertes")
    suspend fun createAlerte(@Body alerte: AlerteDto): AlerteDto

    @GET("alertes")
    suspend fun getAllAlertes(): List<AlerteDto>

    @GET("alertes/{id}")
    suspend fun getAlerteById(@Path("id") id: Long): AlerteDto

    @PUT("alertes/{id}")
    suspend fun updateAlerte(@Path("id") id: Long, @Body alerte: AlerteDto): AlerteDto

    @DELETE("alertes/{id}")
    suspend fun deleteAlerte(@Path("id") id: Long)
}