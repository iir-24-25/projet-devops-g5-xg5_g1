package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.local.converters.RoomConverters
import java.time.LocalDateTime

@Entity(
    tableName = "alertes",
    foreignKeys = [
        ForeignKey(
            entity = LotEntity::class,
            parentColumns = ["id"],
            childColumns = ["lotId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("lotId")]
)
@TypeConverters(RoomConverters::class)
data class AlerteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TypeAlert,
    val message: String,
    val estResolue: Boolean,
    val dateAlerte: LocalDateTime,
    val lotId: Long
)