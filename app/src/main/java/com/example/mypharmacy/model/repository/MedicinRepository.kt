package com.example.mypharmacy.model.repository


import com.example.mypharmacy.model.domain.Medicin
import com.example.mypharmacy.model.local.dao.MedicinDao
import com.example.mypharmacy.model.local.entity.MedicinEntity
import com.example.mypharmacy.model.remote.RetrofitClient
import com.example.mypharmacy.model.remote.model.MedicinDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicinRepository @Inject constructor(
    private val medicinDao: MedicinDao
) {
    // Local data operations
    fun getAllMedicins(): Flow<List<Medicin>> {
        return medicinDao.getAllMedicins().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getMedicinById(id: Long): Flow<Medicin?> {
        return medicinDao.getMedicinById(id).map { it?.toDomainModel() }
    }

    fun getMedicinsByUserId(userId: String): Flow<List<Medicin>> {
        return medicinDao.getMedicinsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLowStockMedicins(): Flow<List<Medicin>> {
        return medicinDao.getLowStockMedicins().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getLowStockMedicinsByUserId(userId: String): Flow<List<Medicin>> {
        return medicinDao.getLowStockMedicinsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun insertMedicin(medicin: Medicin): Long {
        return medicinDao.insertMedicin(medicin.toEntity())
    }

    suspend fun updateMedicin(medicin: Medicin) {
        medicinDao.updateMedicin(medicin.toEntity())
    }

    suspend fun deleteMedicin(medicin: Medicin) {
        medicinDao.deleteMedicin(medicin.toEntity())
    }

    // Remote data operations
    suspend fun createMedicin(medicin: Medicin): Medicin {
        val response = RetrofitClient.medicinApi.createMedicin(medicin.toDto())
        val createdMedicin = response.toDomainModel()
        // Save to local database
        insertMedicin(createdMedicin)
        return createdMedicin
    }

    suspend fun updateMedicinRemote(id: Long, medicin: Medicin): Medicin {
        val response = RetrofitClient.medicinApi.updateMedicin(id, medicin.toDto())
        val updatedMedicin = response.toDomainModel()
        // Update local database
        updateMedicin(updatedMedicin)
        return updatedMedicin
    }

    suspend fun deleteMedicinRemote(id: Long) {
        RetrofitClient.medicinApi.deleteMedicin(id)
        // Could delete from local database here if needed
    }

    suspend fun getMedicinsRemote(userId: String? = null): List<Medicin> {
        val medicinDtos = RetrofitClient.medicinApi.getMedicins(userId)
        val medicins = medicinDtos.map { it.toDomainModel() }
        // Save to local database
        medicins.forEach { insertMedicin(it) }
        return medicins
    }

    suspend fun getMedicinByIdRemote(id: Long): Medicin {
        val response = RetrofitClient.medicinApi.getMedicinById(id)
        val medicin = response.toDomainModel()
        // Save to local database
        insertMedicin(medicin)
        return medicin
    }

    suspend fun getLowStockMedicinsRemote(userId: String? = null): List<Medicin> {
        val medicinDtos = RetrofitClient.medicinApi.getLowStockMedicins(userId)
        val medicins = medicinDtos.map { it.toDomainModel() }
        // Save to local database
        medicins.forEach { insertMedicin(it) }
        return medicins
    }

    // Function to sync local and remote data
    suspend fun syncMedicins(userId: String? = null) {
        val remoteMedicins = getMedicinsRemote(userId)
        // Implementation logic for syncing
    }

    // Mapper functions
    private fun MedicinEntity.toDomainModel(): Medicin {
        return Medicin(
            id = id,
            name = name,
            description = description,
            codeBarres = codeBarres,
            categorie = categorie,
            fabriquant = fabriquant,
            seuilAlerte = seuilAlerte,
            quantity = quantity,
            userId = userId
        )
    }

    private fun Medicin.toEntity(): MedicinEntity {
        return MedicinEntity(
            id = id,
            name = name,
            description = description,
            codeBarres = codeBarres,
            categorie = categorie,
            fabriquant = fabriquant,
            seuilAlerte = seuilAlerte,
            quantity = quantity,
            userId = userId
        )
    }

    private fun Medicin.toDto(): MedicinDto {
        return MedicinDto(
            id = if (id > 0) id else null,
            name = name,
            description = description,
            codeBarres = codeBarres,
            categorie = categorie,
            fabriquant = fabriquant,
            seuilAlerte = seuilAlerte,
            quantity = quantity,
            userId = userId
        )
    }

    private fun MedicinDto.toDomainModel(): Medicin {
        return Medicin(
            id = id ?: 0,
            name = name,
            description = description,
            codeBarres = codeBarres,
            categorie = categorie,
            fabriquant = fabriquant,
            seuilAlerte = seuilAlerte,
            quantity = quantity,
            userId = userId
        )
    }
}