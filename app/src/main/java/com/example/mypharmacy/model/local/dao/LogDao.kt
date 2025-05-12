package com.example.mypharmacy.model.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mypharmacy.model.local.entity.LogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntity): Long

    @Update
    suspend fun updateLog(log: LogEntity)

    @Delete
    suspend fun deleteLog(log: LogEntity)

    @Query("SELECT * FROM logs ORDER BY dateAction DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE id = :id")
    fun getLogById(id: Long): Flow<LogEntity?>

    @Query("SELECT * FROM logs WHERE utilisateurId = :userId ORDER BY dateAction DESC")
    fun getLogsByUserId(userId: Long): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE dateAction BETWEEN :startDate AND :endDate ORDER BY dateAction DESC")
    fun getLogsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE `action` LIKE '%' || :keyword || '%' ORDER BY dateAction DESC")
    fun searchLogs(keyword: String): Flow<List<LogEntity>>
}