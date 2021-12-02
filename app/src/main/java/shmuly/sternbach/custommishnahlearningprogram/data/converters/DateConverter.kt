package shmuly.sternbach.custommishnahlearningprogram.data.converters

import io.objectbox.converter.PropertyConverter
import java.time.LocalDate

class DateConverter: PropertyConverter<LocalDate?, String> {

    override fun convertToEntityProperty(databaseValue: String?): LocalDate? {
        return databaseValue?.let { LocalDate.parse(it) }
    }

    override fun convertToDatabaseValue(entityProperty: LocalDate?): String? {
        return entityProperty?.toString()
    }
}