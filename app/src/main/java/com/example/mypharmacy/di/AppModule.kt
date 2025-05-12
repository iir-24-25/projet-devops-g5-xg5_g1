package com.example.mypharmacy.di

import android.content.Context
import com.example.mypharmacy.model.local.MyPharmacyDatabase
import com.example.mypharmacy.model.local.dao.AlerteDao
import com.example.mypharmacy.model.local.dao.LogDao
import com.example.mypharmacy.model.local.dao.LotDao
import com.example.mypharmacy.model.local.dao.MedicinDao
import com.example.mypharmacy.model.local.dao.MouvementStockDao
import com.example.mypharmacy.model.local.dao.UserDao
import com.example.mypharmacy.model.repository.AlerteRepository
import com.example.mypharmacy.model.repository.LogRepository
import com.example.mypharmacy.model.repository.MedicinRepository
import com.example.mypharmacy.model.repository.MouvementStockRepository
import com.example.mypharmacy.model.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MyPharmacyDatabase {
        return MyPharmacyDatabase.getDatabase(context)
    }

    // DAOs
    @Provides
    @Singleton
    fun provideUserDao(database: MyPharmacyDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideMedicinDao(database: MyPharmacyDatabase): MedicinDao {
        return database.medicinDao()
    }

    @Provides
    @Singleton
    fun provideLotDao(database: MyPharmacyDatabase): LotDao {
        return database.lotDao()
    }

    @Provides
    @Singleton
    fun provideAlerteDao(database: MyPharmacyDatabase): AlerteDao {
        return database.alerteDao()
    }

    @Provides
    @Singleton
    fun provideLogDao(database: MyPharmacyDatabase): LogDao {
        return database.logDao()
    }

    @Provides
    @Singleton
    fun provideMouvementStockDao(database: MyPharmacyDatabase): MouvementStockDao {
        return database.mouvementStockDao()
    }

    // Repositories
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideMedicinRepository(medicinDao: MedicinDao): MedicinRepository {
        return MedicinRepository(medicinDao)
    }

    @Provides
    @Singleton
    fun provideAlerteRepository(
        alerteDao: AlerteDao,
        lotDao: LotDao
    ): AlerteRepository {
        return AlerteRepository(alerteDao, lotDao)
    }

    @Provides
    @Singleton
    fun provideLogRepository(logDao: LogDao): LogRepository {
        return LogRepository(logDao)
    }

    @Provides
    @Singleton
    fun provideMouvementStockRepository(mouvementStockDao: MouvementStockDao): MouvementStockRepository {
        return MouvementStockRepository(mouvementStockDao)


    }
}