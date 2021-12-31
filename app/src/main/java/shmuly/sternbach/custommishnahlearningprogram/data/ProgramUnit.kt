/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shmuly.sternbach.custommishnahlearningprogram.data

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import shmuly.sternbach.custommishnahlearningprogram.data.converters.DateConverter
import shmuly.sternbach.custommishnahlearningprogram.data.converters.NewMaterialConverter
import shmuly.sternbach.custommishnahlearningprogram.data.converters.ReviewConverter
import java.time.LocalDate

/**
 * This is the object displayed in a list of things to learn
 */

//@E//ntity(tableName = "units_table")
//@T//ypeConverters(Converters::class)
@Entity
data class ProgramUnit(
    @Id
    var id: Long = 0,
    @Convert(converter = DateConverter::class, dbType = String::class)
    var date: LocalDate,
    @Convert(converter = NewMaterialConverter::class, dbType = String::class)
    val newMaterial: List<ProgramUnitMolecule>,
    @Convert(converter = ReviewConverter::class, dbType = String::class)
    val reviews: List<List<ProgramUnitMolecule>>,
    val programID: Int,
) {
    override fun toString(): String {
        return if (newMaterial.isEmpty()) "" else newMaterial.first()
            .toString() + " - " + newMaterial.last().toString()
    }
}
/*@Entity
data class CompletedUnit(
    @Id
    var id: Long = 0,
    @Convert(converter = ProgramUnitConverter::class, dbType = String::class)
    override var programUnitMaterial: ProgramUnitMaterial<String>? = null,
    @Convert(converter = DateConverter::class, dbType = String::class)
    override var date: LocalDate? = null
)*/
