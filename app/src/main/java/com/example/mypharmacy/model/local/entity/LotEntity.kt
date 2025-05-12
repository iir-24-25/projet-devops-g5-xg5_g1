package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mypharmacy.model.local.converters.RoomConverters
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "lots",
    foreignKeys = [
        ForeignKey(
            entity = MedicinEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicinId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicinId")]
)
@TypeConverters(RoomConverters::class)
data class LotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numeroLot: String,
    val dateExpiration: LocalDate,
    val dateEntree: LocalDateTime,
    val quantite: Int,
    val medicinId: Long,
    val userId: String
)