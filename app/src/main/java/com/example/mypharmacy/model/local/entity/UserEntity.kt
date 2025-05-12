package com.example.mypharmacy.model.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.local.converters.RoomConverters

@Entity(tableName = "users")
@TypeConverters(RoomConverters::class)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val email: String,
    val role: Role,
    val token: String? = null
)