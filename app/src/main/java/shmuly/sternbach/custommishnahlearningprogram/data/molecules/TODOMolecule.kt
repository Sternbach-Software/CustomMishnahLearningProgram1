package shmuly.sternbach.custommishnahlearningprogram.data.molecules

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import shmuly.sternbach.custommishnahlearningprogram.data.converters.DateConverter
import java.time.LocalDate

@Entity
data class TODOMolecule(
    @Id
    var id: Long = 0,
    @Index
    var molecule: String? = null,
    @Convert(converter = DateConverter::class, dbType = String::class)
    var date: LocalDate? = null,
)