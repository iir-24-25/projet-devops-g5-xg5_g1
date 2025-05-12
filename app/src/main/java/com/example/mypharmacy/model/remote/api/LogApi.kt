package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.LogDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LogApi {
    @POST("log")
    suspend fun createLog(@Body log: LogDto): LogDto

    @GET("log")
    suspend fun getAllLogs(): List<LogDto>

    @GET("log/{id}")
    suspend fun getLogById(@Path("id") id: Long): LogDto

    @PUT("log/{id}")
    suspend fun updateLog(@Path("id") id: Long, @Body log: LogDto): LogDto

    @DELETE("log/{id}")
    suspend fun deleteLog(@Path("id") id: Long)
}