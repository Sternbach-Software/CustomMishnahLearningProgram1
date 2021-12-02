package shmuly.sternbach.custommishnahlearningprogram.data

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.data.converters.ProgramConverter
import shmuly.sternbach.custommishnahlearningprogram.data.units.ProgramUnitMaterial
import java.time.LocalDate

@Entity
data class Program(
    @Id
    var id: Long = 0,
    @Convert(converter = ProgramConverter::class, dbType = String::class)
    var program: Map<
            LocalDate,
            MutablePair<
                    ProgramUnitMaterial<String>?,
                    MutableList<
                            ProgramUnitMaterial<String>
                            >
                    >
            >? = null
)
