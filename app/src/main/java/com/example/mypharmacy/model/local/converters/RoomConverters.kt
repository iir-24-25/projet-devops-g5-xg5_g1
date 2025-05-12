package com.example.mypharmacy.model.local.converters


import androidx.room.TypeConverter
import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.domain.TypeMouvement
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomConverters {
    // Date converters
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    // Enum converters
    @TypeConverter
    fun fromRole(value: Role?): String? {
        return value?.name
    }

    @TypeConverter
    fun toRole(value: String?): Role? {
        return value?.let { Role.valueOf(it) }
    }

    @TypeConverter
    fun fromTypeAlert(value: TypeAlert?): String? {
        return value?.name
    }

    @TypeConverter
    fun toTypeAlert(value: String?): TypeAlert? {
        return value?.let { TypeAlert.valueOf(it) }
    }

    @TypeConverter
    fun fromTypeMouvement(value: TypeMouvement?): String? {
        return value?.name
    }

    @TypeConverter
    fun toTypeMouvement(value: String?): TypeMouvement? {
        return value?.let { TypeMouvement.valueOf(it) }
    }
}