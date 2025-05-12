package com.example.mypharmacy.model.remote.api


import com.example.mypharmacy.model.remote.model.MedicinDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MedicinApi {
    @POST("medicins")
    suspend fun createMedicin(@Body medicin: MedicinDto): MedicinDto

    @PUT("medicins/{id}")
    suspend fun updateMedicin(@Path("id") id: Long, @Body medicin: MedicinDto): MedicinDto

    @DELETE("medicins/{id}")
    suspend fun deleteMedicin(@Path("id") id: Long)

    @GET("medicins")
    suspend fun getMedicins(@Query("userId") userId: String? = null): List<MedicinDto>

    @GET("medicins/{id}")
    suspend fun getMedicinById(@Path("id") id: Long): MedicinDto

    @GET("medicins/low-stock")
    suspend fun getLowStockMedicins(@Query("userId") userId: String? = null): List<MedicinDto>
}