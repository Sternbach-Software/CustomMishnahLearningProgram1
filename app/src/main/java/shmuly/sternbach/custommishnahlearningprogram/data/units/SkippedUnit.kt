package shmuly.sternbach.custommishnahlearningprogram.data.units

import io.objectbox.annotation.*
import shmuly.sternbach.custommishnahlearningprogram.data.converters.DateConverter
import shmuly.sternbach.custommishnahlearningprogram.data.converters.ProgramUnitConverter
import java.time.LocalDate

@Entity
data class SkippedUnit(
    @Id
    var id: Long = 0,
    @Convert(converter = ProgramUnitConverter::class, dbType = String::class)
    @Index(type=IndexType.HASH)
    override var programUnitMaterial: ProgramUnitMaterial<String>? = null,
    @Convert(converter = DateConverter::class, dbType = String::class)
    override var date: LocalDate? = null
): ProgramUnitDated<String>
