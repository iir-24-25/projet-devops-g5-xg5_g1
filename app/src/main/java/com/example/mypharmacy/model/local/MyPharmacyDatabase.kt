package com.example.mypharmacy.model.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mypharmacy.model.local.converters.RoomConverters
import com.example.mypharmacy.model.local.dao.*
import com.example.mypharmacy.model.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        MedicinEntity::class,
        LotEntity::class,
        AlerteEntity::class,
        LogEntity::class,
        MouvementStockEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class MyPharmacyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun medicinDao(): MedicinDao
    abstract fun lotDao(): LotDao
    abstract fun alerteDao(): AlerteDao
    abstract fun logDao(): LogDao
    abstract fun mouvementStockDao(): MouvementStockDao

    companion object {
        @Volatile
        private var INSTANCE: MyPharmacyDatabase? = null

        fun getDatabase(context: Context): MyPharmacyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyPharmacyDatabase::class.java,
                    "my_pharmacy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}