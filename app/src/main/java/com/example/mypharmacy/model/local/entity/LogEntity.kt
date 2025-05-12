package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mypharmacy.model.local.converters.RoomConverters
import java.time.LocalDateTime

@Entity(
    tableName = "logs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["utilisateurId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("utilisateurId")]
)
@TypeConverters(RoomConverters::class)
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val action: String,
    val dateAction: LocalDateTime,
    val utilisateurId: Long
)