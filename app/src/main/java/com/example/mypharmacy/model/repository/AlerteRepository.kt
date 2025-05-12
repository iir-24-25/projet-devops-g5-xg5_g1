package com.example.mypharmacy.model.repository


import com.example.mypharmacy.model.domain.Alerte
import com.example.mypharmacy.model.domain.Lot
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.local.dao.AlerteDao
import com.example.mypharmacy.model.local.dao.LotDao
import com.example.mypharmacy.model.local.entity.AlerteEntity
import com.example.mypharmacy.model.remote.RetrofitClient
import com.example.mypharmacy.model.remote.model.AlerteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlerteRepository @Inject constructor(
    private val alerteDao: AlerteDao,
    private val lotDao: LotDao
) {
    // Local data operations
    fun getAllAlertes(): Flow<List<Alerte>> {
        return alerteDao.getAllAlertes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getAlerteById(id: Long): Flow<Alerte?> {
        return alerteDao.getAlerteById(id).map { it?.toDomainModel() }
    }

    fun getAlertesByLotId(lotId: Long): Flow<List<Alerte>> {
        return alerteDao.getAlertesByLotId(lotId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getAlertesByType(type: TypeAlert): Flow<List<Alerte>> {
        return alerteDao.getAlertesByType(type).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getAlertesByUserId(userId: String): Flow<List<Alerte>> {
        return alerteDao.getAlertesByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getActiveAlertes(): Flow<List<Alerte>> {
        return alerteDao.getActiveAlertes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getActiveAlertesByUserId(userId: String): Flow<List<Alerte>> {
        return alerteDao.getActiveAlertesByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun insertAlerte(alerte: Alerte): Long {
        return alerteDao.insertAlerte(alerte.toEntity())
    }

    suspend fun updateAlerte(alerte: Alerte) {
        alerteDao.updateAlerte(alerte.toEntity())
    }

    suspend fun deleteAlerte(alerte: Alerte) {
        alerteDao.deleteAlerte(alerte.toEntity())
    }

    suspend fun markAlerteAsResolved(alerteId: Long) {
        alerteDao.markAlerteAsResolved(alerteId)
    }

    // Remote data operations
    suspend fun createAlerte(alerte: Alerte): Alerte {
        val response = RetrofitClient.alerteApi.createAlerte(alerte.toDto())
        val createdAlerte = response.toDomainModel()
        // Save to local database
        insertAlerte(createdAlerte)
        return createdAlerte
    }

    suspend fun updateAlerteRemote(id: Long, alerte: Alerte): Alerte {
        val response = RetrofitClient.alerteApi.updateAlerte(id, alerte.toDto())
        val updatedAlerte = response.toDomainModel()
        // Update local database
        updateAlerte(updatedAlerte)
        return updatedAlerte
    }

    suspend fun deleteAlerteRemote(id: Long) {
        RetrofitClient.alerteApi.deleteAlerte(id)
        // Could delete from local database if needed
    }

    suspend fun getAllAlertesRemote(): List<Alerte> {
        val alerteDtos = RetrofitClient.alerteApi.getAllAlertes()
        val alertes = alerteDtos.map { it.toDomainModel() }
        // Save to local database
        alertes.forEach { insertAlerte(it) }
        return alertes
    }

    suspend fun getAlerteByIdRemote(id: Long): Alerte {
        val response = RetrofitClient.alerteApi.getAlerteById(id)
        val alerte = response.toDomainModel()
        // Save to local database
        insertAlerte(alerte)
        return alerte
    }

    // Function to sync local and remote data
    suspend fun syncAlertes() {
        val remoteAlertes = getAllAlertesRemote()
        // Implementation logic for syncing
    }

    // Auto-detection of low stock and expiring lots
    suspend fun checkForLowStockAlerts(medicin: Long, quantity: Int, seuilAlerte: Int, lotId: Long) {
        if (quantity <= seuilAlerte) {
            val alerte = Alerte(
                type = TypeAlert.STOCK,
                message = "Stock bas pour le médicament ID $medicin: $quantity unités restantes",
                estResolue = false,
                dateAlerte = java.time.LocalDateTime.now(),
                lotId = lotId
            )
            insertAlerte(alerte)
        }
    }

    // Mapper functions
    private fun AlerteEntity.toDomainModel(): Alerte {
        return Alerte(
            id = id,
            type = type,
            message = message,
            estResolue = estResolue,
            dateAlerte = dateAlerte,
            lotId = lotId
        )
    }

    private fun Alerte.toEntity(): AlerteEntity {
        return AlerteEntity(
            id = id,
            type = type,
            message = message,
            estResolue = estResolue,
            dateAlerte = dateAlerte,
            lotId = lotId
        )
    }

    private fun Alerte.toDto(): AlerteDto {
        return AlerteDto(
            id = if (id > 0) id else null,
            type = type,
            message = message,
            estResolue = estResolue,
            dateAlerte = dateAlerte,
            lotId = lotId
        )
    }

    private fun AlerteDto.toDomainModel(): Alerte {
        return Alerte(
            id = id ?: 0,
            type = type,
            message = message,
            estResolue = estResolue,
            dateAlerte = dateAlerte,
            lotId = lotId
        )
    }
}