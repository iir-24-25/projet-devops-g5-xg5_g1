package com.example.mypharmacy.model.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.local.entity.AlerteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlerteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerte(alerte: AlerteEntity): Long

    @Update
    suspend fun updateAlerte(alerte: AlerteEntity)

    @Delete
    suspend fun deleteAlerte(alerte: AlerteEntity)

    @Query("SELECT * FROM alertes")
    fun getAllAlertes(): Flow<List<AlerteEntity>>

    @Query("SELECT * FROM alertes WHERE id = :id")
    fun getAlerteById(id: Long): Flow<AlerteEntity?>

    @Query("SELECT * FROM alertes WHERE lotId = :lotId")
    fun getAlertesByLotId(lotId: Long): Flow<List<AlerteEntity>>

    @Query("SELECT * FROM alertes WHERE type = :type")
    fun getAlertesByType(type: TypeAlert): Flow<List<AlerteEntity>>

    @Query("SELECT a.* FROM alertes a JOIN lots l ON a.lotId = l.id WHERE l.userId = :userId")
    fun getAlertesByUserId(userId: String): Flow<List<AlerteEntity>>

    @Query("SELECT * FROM alertes WHERE estResolue = 0")
    fun getActiveAlertes(): Flow<List<AlerteEntity>>

    @Query("SELECT a.* FROM alertes a JOIN lots l ON a.lotId = l.id WHERE l.userId = :userId AND a.estResolue = 0")
    fun getActiveAlertesByUserId(userId: String): Flow<List<AlerteEntity>>

    @Query("UPDATE alertes SET estResolue = 1 WHERE id = :alerteId")
    suspend fun markAlerteAsResolved(alerteId: Long)
}