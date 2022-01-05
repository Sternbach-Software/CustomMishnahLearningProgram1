package shmuly.sternbach.custommishnahlearningprogram.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Converters {
    @TypeConverter
    fun convertToEntityProperty(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun convertToDatabaseValue(entityProperty: LocalDate): String {
        return entityProperty.toString()
    }
}