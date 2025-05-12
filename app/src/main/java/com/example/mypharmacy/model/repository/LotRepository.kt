package com.example.mypharmacy.model.repository


import com.example.mypharmacy.model.domain.Lot
import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.model.local.dao.LotDao
import com.example.mypharmacy.model.local.dao.MedicinDao
import com.example.mypharmacy.model.local.entity.LotEntity
import com.example.mypharmacy.model.remote.model.LotDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LotRepository @Inject constructor(
    private val lotDao: LotDao,
    private val medicinDao: MedicinDao
) {
    // Local data operations
    fun getAllLots(): Flow<List<Lot>> {
        return lotDao.getAllLots().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLotById(id: Long): Flow<Lot?> {
        return lotDao.getLotById(id).map { it?.toDomainModel() }
    }

    fun getLotsByMedicinId(medicinId: Long): Flow<List<Lot>> {
        return lotDao.getLotsByMedicinId(medicinId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLotsByUserId(userId: String): Flow<List<Lot>> {
        return lotDao.getLotsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getExpiringLots(date: LocalDate): Flow<List<Lot>> {
        return lotDao.getExpiringLots(date).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getExpiringLotsByUserId(userId: String, date: LocalDate): Flow<List<Lot>> {
        return lotDao.getExpiringLotsByUserId(userId, date).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun insertLot(lot: Lot): Long {
        return lotDao.insertLot(lot.toEntity())
    }

    suspend fun updateLot(lot: Lot) {
        lotDao.updateLot(lot.toEntity())
    }

    suspend fun deleteLot(lot: Lot) {
        lotDao.deleteLot(lot.toEntity())
    }

    // We don't have a dedicated LotAPI in the backend, so we'll work with just local operations
    // Or implement custom API calls if needed in the future

    // Helper function to check for expiring lots
    suspend fun checkForExpiringLots(days: Int = 30) {
        val expirationDate = LocalDate.now().plusDays(days.toLong())
        // Implementation logic for checking expiring lots
    }

    // Mapper functions
    private fun LotEntity.toDomainModel(): Lot {
        return Lot(
            id = id,
            numeroLot = numeroLot,
            dateExpiration = dateExpiration,
            dateEntree = dateEntree,
            quantite = quantite,
            medicinId = medicinId,
            userId = userId
        )
    }

    private fun Lot.toEntity(): LotEntity {
        return LotEntity(
            id = id,
            numeroLot = numeroLot,
            dateExpiration = dateExpiration,
            dateEntree = dateEntree,
            quantite = quantite,
            medicinId = medicinId,
            userId = userId
        )
    }

    private fun Lot.toDto(): LotDto {
        return LotDto(
            id = if (id > 0) id else null,
            numeroLot = numeroLot,
            dateExpiration = dateExpiration,
            dateEntree = dateEntree,
            quantite = quantite,
            medicinId = medicinId,
            userId = userId
        )
    }

    private fun LotDto.toDomainModel(): Lot {
        return Lot(
            id = id ?: 0,
            numeroLot = numeroLot,
            dateExpiration = dateExpiration,
            dateEntree = dateEntree,
            quantite = quantite,
            medicinId = medicinId,
            userId = userId
        )
    }}