package shmuly.sternbach.custommishnahlearningprogram.data

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.data.converters.ProgramUnitConverter
import java.time.LocalDate

@Entity
data class Program(
    @Id
    var id: Long = 0,
    @Convert(converter = ProgramUnitConverter::class, dbType = String::class)
    var program: Map<LocalDate, MutablePair<ProgramUnit<String>?, MutableList<ProgramUnit<String>>>>? = null
)
