package com.example.mypharmacy.model.repository

import android.util.Log
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
        Log.d("MedicinRepository", "Inserting medicine to local DB: ${medicin.name}")
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
        try {
            Log.d("MedicinRepository", "Calling remote API to create medicin: ${medicin.name}")

            val medicinDto = medicin.toDto()
            Log.d("MedicinRepository", "Converted to DTO: $medicinDto")

            val response = RetrofitClient.medicinApi.createMedicin(medicinDto)
            Log.d("MedicinRepository", "Remote API returned response with ID: ${response.id}")

            val createdMedicin = response.toDomainModel()
            Log.d("MedicinRepository", "Converted response to domain model: ${createdMedicin.id}")

            // Save to local database
            insertMedicin(createdMedicin)
            Log.d("MedicinRepository", "Saved to local database")

            return createdMedicin
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error calling remote API: ${e.message}", e)
            throw e
        }
    }

    suspend fun updateMedicinRemote(id: Long, medicin: Medicin): Medicin {
        try {
            Log.d("MedicinRepository", "Updating medicine remotely: ID $id, Name: ${medicin.name}")
            val response = RetrofitClient.medicinApi.updateMedicin(id, medicin.toDto())
            val updatedMedicin = response.toDomainModel()
            // Update local database
            updateMedicin(updatedMedicin)
            return updatedMedicin
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error updating medicine remotely: ${e.message}", e)
            throw e
        }
    }

    suspend fun deleteMedicinRemote(id: Long) {
        try {
            Log.d("MedicinRepository", "Deleting medicine remotely: ID $id")
            RetrofitClient.medicinApi.deleteMedicin(id)
            // Could delete from local database here if needed
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error deleting medicine remotely: ${e.message}", e)
            throw e
        }
    }

    suspend fun getMedicinsRemote(userId: String? = null): List<Medicin> {
        try {
            Log.d("MedicinRepository", "Getting medicines remotely" + if (userId != null) " for user $userId" else "")
            val medicinDtos = RetrofitClient.medicinApi.getMedicins(userId)
            val medicins = medicinDtos.map { it.toDomainModel() }
            // Save to local database
            medicins.forEach { insertMedicin(it) }
            return medicins
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error getting medicines remotely: ${e.message}", e)
            throw e
        }
    }

    suspend fun getMedicinByIdRemote(id: Long): Medicin {
        try {
            Log.d("MedicinRepository", "Getting medicine by ID remotely: $id")
            val response = RetrofitClient.medicinApi.getMedicinById(id)
            val medicin = response.toDomainModel()
            // Save to local database
            insertMedicin(medicin)
            return medicin
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error getting medicine by ID remotely: ${e.message}", e)
            throw e
        }
    }

    suspend fun getLowStockMedicinsRemote(userId: String? = null): List<Medicin> {
        try {
            Log.d("MedicinRepository", "Getting low stock medicines remotely" + if (userId != null) " for user $userId" else "")
            val medicinDtos = RetrofitClient.medicinApi.getLowStockMedicins(userId)
            val medicins = medicinDtos.map { it.toDomainModel() }
            // Save to local database
            medicins.forEach { insertMedicin(it) }
            return medicins
        } catch (e: Exception) {
            Log.e("MedicinRepository", "Error getting low stock medicines remotely: ${e.message}", e)
            throw e
        }
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