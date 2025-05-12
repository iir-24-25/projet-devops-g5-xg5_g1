package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.model.local.converters.RoomConverters
import java.time.LocalDateTime

@Entity(
    tableName = "mouvements_stock",
    foreignKeys = [
        ForeignKey(
            entity = LotEntity::class,
            parentColumns = ["id"],
            childColumns = ["lotId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["utilisateurId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("lotId"), Index("utilisateurId")]
)
@TypeConverters(RoomConverters::class)
data class MouvementStockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val motif: String,
    val dateMouvement: LocalDateTime,
    val type: TypeMouvement,
    val lotId: Long,
    val utilisateurId: Long,
    val quantite: Int
)