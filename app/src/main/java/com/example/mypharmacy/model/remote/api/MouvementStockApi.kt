package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.MouvementStockDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MouvementStockApi {
    @POST("mouvements")
    suspend fun createMouvement(@Body mouvement: MouvementStockDto): MouvementStockDto

    @GET("mouvements")
    suspend fun getAllMouvements(): List<MouvementStockDto>

    @GET("mouvements/{id}")
    suspend fun getMouvementById(@Path("id") id: Long): MouvementStockDto

    @PUT("mouvements/{id}")
    suspend fun updateMouvement(@Path("id") id: Long, @Body mouvement: MouvementStockDto): MouvementStockDto

    @DELETE("mouvements/{id}")
    suspend fun deleteMouvement(@Path("id") id: Long)
}