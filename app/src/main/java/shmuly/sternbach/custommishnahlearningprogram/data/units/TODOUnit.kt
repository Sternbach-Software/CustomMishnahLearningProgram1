package shmuly.sternbach.custommishnahlearningprogram.data.units

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import shmuly.sternbach.custommishnahlearningprogram.data.converters.DateConverter
import shmuly.sternbach.custommishnahlearningprogram.data.converters.ProgramUnitConverter
import java.time.LocalDate

@Entity
data class TODOUnit(
    @Id
    var id: Long = 0,
    @Convert(converter = ProgramUnitConverter::class, dbType = String::class)
    override var programUnitMaterial: ProgramUnitMaterial<String>? = null,
    @Convert(converter = DateConverter::class, dbType = String::class)
    override var date: LocalDate? = null
): ProgramUnitDated<String>
