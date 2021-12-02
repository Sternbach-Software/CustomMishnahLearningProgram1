package shmuly.sternbach.custommishnahlearningprogram.data.converters

import io.objectbox.converter.PropertyConverter
import shmuly.sternbach.custommishnahlearningprogram.data.units.ProgramUnitMaterial

class ProgramUnitConverter: PropertyConverter<ProgramUnitMaterial<String>?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): ProgramUnitMaterial<String>? {
        return databaseValue?.split("~")?.let { ProgramUnitMaterial(it) }
    }

    override fun convertToDatabaseValue(entityProperty: ProgramUnitMaterial<String>?): String? {
        return entityProperty?.listOfMaterials?.joinToString("~")
    }
}