package shmuly.sternbach.custommishnahlearningprogram.data

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import shmuly.sternbach.custommishnahlearningprogram.data.converters.DateConverter
import java.time.LocalDate

@Entity
data class SkippedUnit(
    @Id
    var id: Long = 0,
    @Convert(converter = DateConverter::class, dbType = String::class)
    var date: LocalDate? = null
    )
