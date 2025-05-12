package com.example.mypharmacy.model.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.model.local.entity.MouvementStockEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MouvementStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMouvement(mouvement: MouvementStockEntity): Long

    @Update
    suspend fun updateMouvement(mouvement: MouvementStockEntity)

    @Delete
    suspend fun deleteMouvement(mouvement: MouvementStockEntity)

    @Query("SELECT * FROM mouvements_stock ORDER BY dateMouvement DESC")
    fun getAllMouvements(): Flow<List<MouvementStockEntity>>

    @Query("SELECT * FROM mouvements_stock WHERE id = :id")
    fun getMouvementById(id: Long): Flow<MouvementStockEntity?>

    @Query("SELECT * FROM mouvements_stock WHERE lotId = :lotId ORDER BY dateMouvement DESC")
    fun getMouvementsByLotId(lotId: Long): Flow<List<MouvementStockEntity>>

    @Query("SELECT * FROM mouvements_stock WHERE utilisateurId = :userId ORDER BY dateMouvement DESC")
    fun getMouvementsByUserId(userId: Long): Flow<List<MouvementStockEntity>>

    @Query("SELECT * FROM mouvements_stock WHERE type = :type ORDER BY dateMouvement DESC")
    fun getMouvementsByType(type: TypeMouvement): Flow<List<MouvementStockEntity>>

    @Query("SELECT * FROM mouvements_stock WHERE dateMouvement BETWEEN :startDate AND :endDate ORDER BY dateMouvement DESC")
    fun getMouvementsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MouvementStockEntity>>

    @Query("SELECT ms.* FROM mouvements_stock ms JOIN lots l ON ms.lotId = l.id WHERE l.medicinId = :medicinId ORDER BY ms.dateMouvement DESC")
    fun getMouvementsByMedicinId(medicinId: Long): Flow<List<MouvementStockEntity>>

    @Query("SELECT ms.* FROM mouvements_stock ms JOIN lots l ON ms.lotId = l.id WHERE l.userId = :userId ORDER BY ms.dateMouvement DESC")
    fun getMouvementsByLotUserId(userId: String): Flow<List<MouvementStockEntity>>
}