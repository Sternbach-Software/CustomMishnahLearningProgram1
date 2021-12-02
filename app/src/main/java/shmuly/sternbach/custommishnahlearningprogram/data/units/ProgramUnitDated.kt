package shmuly.sternbach.custommishnahlearningprogram.data.units

import io.objectbox.annotation.BaseEntity
import java.time.LocalDate
@BaseEntity
interface ProgramUnitDated<T> {
    val programUnitMaterial: ProgramUnitMaterial<String>?
    val date: LocalDate?
}