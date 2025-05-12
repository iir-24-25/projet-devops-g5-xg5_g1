package com.example.mypharmacy.model.repository


import com.example.mypharmacy.model.domain.Log
import com.example.mypharmacy.model.local.dao.LogDao
import com.example.mypharmacy.model.local.entity.LogEntity
import com.example.mypharmacy.model.remote.RetrofitClient
import com.example.mypharmacy.model.remote.model.LogDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(
    private val logDao: LogDao
) {
    // Local data operations
    fun getAllLogs(): Flow<List<Log>> {
        return logDao.getAllLogs().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLogById(id: Long): Flow<Log?> {
        return logDao.getLogById(id).map { it?.toDomainModel() }
    }

    fun getLogsByUserId(userId: Long): Flow<List<Log>> {
        return logDao.getLogsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLogsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Log>> {
        return logDao.getLogsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun searchLogs(keyword: String): Flow<List<Log>> {
        return logDao.searchLogs(keyword).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun insertLog(log: Log): Long {
        return logDao.insertLog(log.toEntity())
    }

    suspend fun updateLog(log: Log) {
        logDao.updateLog(log.toEntity())
    }

    suspend fun deleteLog(log: Log) {
        logDao.deleteLog(log.toEntity())
    }

    // Remote data operations
    suspend fun createLog(log: Log): Log {
        val response = RetrofitClient.logApi.createLog(log.toDto())
        val createdLog = response.toDomainModel()
        // Save to local database
        insertLog(createdLog)
        return createdLog
    }

    suspend fun updateLogRemote(id: Long, log: Log): Log {
        val response = RetrofitClient.logApi.updateLog(id, log.toDto())
        val updatedLog = response.toDomainModel()
        // Update local database
        updateLog(updatedLog)
        return updatedLog
    }

    suspend fun deleteLogRemote(id: Long) {
        RetrofitClient.logApi.deleteLog(id)
        // Could delete from local database if needed
    }

    suspend fun getAllLogsRemote(): List<Log> {
        val logDtos = RetrofitClient.logApi.getAllLogs()
        val logs = logDtos.map { it.toDomainModel() }
        // Save to local database
        logs.forEach { insertLog(it) }
        return logs
    }

    suspend fun getLogByIdRemote(id: Long): Log {
        val response = RetrofitClient.logApi.getLogById(id)
        val log = response.toDomainModel()
        // Save to local database
        insertLog(log)
        return log
    }

    // Function to sync local and remote data
    suspend fun syncLogs() {
        val remoteLogs = getAllLogsRemote()
        // Implementation logic for syncing
    }

    // Helper function to create activity logs
    suspend fun logAction(utilisateurId: Long, action: String) {
        val log = Log(
            action = action,
            dateAction = LocalDateTime.now(),
            utilisateurId = utilisateurId
        )
        insertLog(log)
        // Optionally also send to server
        // createLog(log)
    }

    // Mapper functions
    private fun LogEntity.toDomainModel(): Log {
        return Log(
            id = id,
            action = action,
            dateAction = dateAction,
            utilisateurId = utilisateurId
        )
    }

    private fun Log.toEntity(): LogEntity {
        return LogEntity(
            id = id,
            action = action,
            dateAction = dateAction,
            utilisateurId = utilisateurId
        )
    }

    private fun Log.toDto(): LogDto {
        return LogDto(
            id = if (id > 0) id else null,
            action = action,
            dateAction = dateAction,
            utilisateurId = utilisateurId
        )
    }

    private fun LogDto.toDomainModel(): Log {
        return Log(
            id = id ?: 0,
            action = action,
            dateAction = dateAction,
            utilisateurId = utilisateurId
        )
    }
}