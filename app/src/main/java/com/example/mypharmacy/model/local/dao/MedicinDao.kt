package com.example.mypharmacy.model.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mypharmacy.model.local.entity.MedicinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicin(medicin: MedicinEntity): Long

    @Update
    suspend fun updateMedicin(medicin: MedicinEntity)

    @Delete
    suspend fun deleteMedicin(medicin: MedicinEntity)

    @Query("SELECT * FROM medicins")
    fun getAllMedicins(): Flow<List<MedicinEntity>>

    @Query("SELECT * FROM medicins WHERE id = :id")
    fun getMedicinById(id: Long): Flow<MedicinEntity?>

    @Query("SELECT * FROM medicins WHERE userId = :userId")
    fun getMedicinsByUserId(userId: String): Flow<List<MedicinEntity>>

    @Query("SELECT * FROM medicins WHERE quantity <= seuilAlerte AND seuilAlerte > 0")
    fun getLowStockMedicins(): Flow<List<MedicinEntity>>

    @Query("SELECT * FROM medicins WHERE userId = :userId AND quantity <= seuilAlerte AND seuilAlerte > 0")
    fun getLowStockMedicinsByUserId(userId: String): Flow<List<MedicinEntity>>
}