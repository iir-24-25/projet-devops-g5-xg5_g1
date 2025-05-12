package com.example.mypharmacy.model.repository


import com.example.mypharmacy.model.domain.MouvementStock
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.model.local.dao.MouvementStockDao
import com.example.mypharmacy.model.local.entity.MouvementStockEntity
import com.example.mypharmacy.model.remote.RetrofitClient
import com.example.mypharmacy.model.remote.model.MouvementStockDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MouvementStockRepository @Inject constructor(
    private val mouvementStockDao: MouvementStockDao
) {
    // Local data operations
    fun getAllMouvements(): Flow<List<MouvementStock>> {
        return mouvementStockDao.getAllMouvements().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementById(id: Long): Flow<MouvementStock?> {
        return mouvementStockDao.getMouvementById(id).map { it?.toDomainModel() }
    }

    fun getMouvementsByLotId(lotId: Long): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByLotId(lotId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementsByUserId(userId: Long): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementsByType(type: TypeMouvement): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByType(type).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementsByMedicinId(medicinId: Long): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByMedicinId(medicinId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMouvementsByLotUserId(userId: String): Flow<List<MouvementStock>> {
        return mouvementStockDao.getMouvementsByLotUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun insertMouvement(mouvement: MouvementStock): Long {
        return mouvementStockDao.insertMouvement(mouvement.toEntity())
    }

    suspend fun updateMouvement(mouvement: MouvementStock) {
        mouvementStockDao.updateMouvement(mouvement.toEntity())
    }

    suspend fun deleteMouvement(mouvement: MouvementStock) {
        mouvementStockDao.deleteMouvement(mouvement.toEntity())
    }

    // Remote data operations
    suspend fun createMouvement(mouvement: MouvementStock): MouvementStock {
        val response = RetrofitClient.mouvementStockApi.createMouvement(mouvement.toDto())
        val createdMouvement = response.toDomainModel()
        // Save to local database
        insertMouvement(createdMouvement)
        return createdMouvement
    }

    suspend fun updateMouvementRemote(id: Long, mouvement: MouvementStock): MouvementStock {
        val response = RetrofitClient.mouvementStockApi.updateMouvement(id, mouvement.toDto())
        val updatedMouvement = response.toDomainModel()
        // Update local database
        updateMouvement(updatedMouvement)
        return updatedMouvement
    }

    suspend fun deleteMouvementRemote(id: Long) {
        RetrofitClient.mouvementStockApi.deleteMouvement(id)
        // Could delete from local database if needed
    }

    suspend fun getAllMouvementsRemote(): List<MouvementStock> {
        val mouvementDtos = RetrofitClient.mouvementStockApi.getAllMouvements()
        val mouvements = mouvementDtos.map { it.toDomainModel() }
        // Save to local database
        mouvements.forEach { insertMouvement(it) }
        return mouvements
    }

    suspend fun getMouvementByIdRemote(id: Long): MouvementStock {
        val response = RetrofitClient.mouvementStockApi.getMouvementById(id)
        val mouvement = response.toDomainModel()
        // Save to local database
        insertMouvement(mouvement)
        return mouvement
    }

    // Function to sync local and remote data
    suspend fun syncMouvements() {
        val remoteMouvements = getAllMouvementsRemote()
        // Implementation logic for syncing
    }

    // Mapper functions
    private fun MouvementStockEntity.toDomainModel(): MouvementStock {
        return MouvementStock(
            id = id,
            motif = motif,
            dateMouvement = dateMouvement,
            type = type,
            lotId = lotId,
            utilisateurId = utilisateurId,
            quantite = quantite
        )
    }

    private fun MouvementStock.toEntity(): MouvementStockEntity {
        return MouvementStockEntity(
            id = id,
            motif = motif,
            dateMouvement = dateMouvement,
            type = type,
            lotId = lotId,
            utilisateurId = utilisateurId,
            quantite = quantite
        )
    }

    private fun MouvementStock.toDto(): MouvementStockDto {
        return MouvementStockDto(
            id = if (id > 0) id else null,
            motif = motif,
            dateMouvement = dateMouvement,
            type = type,
            lotId = lotId,
            utilisateurId = utilisateurId,
            quantite = quantite
        )
    }

    private fun MouvementStockDto.toDomainModel(): MouvementStock {
        return MouvementStock(
            id = id ?: 0,
            motif = motif,
            dateMouvement = dateMouvement,
            type = type,
            lotId = lotId,
            utilisateurId = utilisateurId,
            quantite = quantite
        )
    }
}