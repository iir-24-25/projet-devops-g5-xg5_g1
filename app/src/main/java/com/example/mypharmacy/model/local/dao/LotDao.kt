package com.example.mypharmacy.model.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mypharmacy.model.local.entity.LotEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface LotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLot(lot: LotEntity): Long

    @Update
    suspend fun updateLot(lot: LotEntity)

    @Delete
    suspend fun deleteLot(lot: LotEntity)

    @Query("SELECT * FROM lots")
    fun getAllLots(): Flow<List<LotEntity>>

    @Query("SELECT * FROM lots WHERE id = :id")
    fun getLotById(id: Long): Flow<LotEntity?>

    @Query("SELECT * FROM lots WHERE medicinId = :medicinId")
    fun getLotsByMedicinId(medicinId: Long): Flow<List<LotEntity>>

    @Query("SELECT * FROM lots WHERE userId = :userId")
    fun getLotsByUserId(userId: String): Flow<List<LotEntity>>

    @Query("SELECT * FROM lots WHERE dateExpiration <= :date")
    fun getExpiringLots(date: LocalDate): Flow<List<LotEntity>>

    @Query("SELECT * FROM lots WHERE userId = :userId AND dateExpiration <= :date")
    fun getExpiringLotsByUserId(userId: String, date: LocalDate): Flow<List<LotEntity>>
}