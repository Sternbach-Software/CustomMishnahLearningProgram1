package shmuly.sternbach.custommishnahlearningprogram.data.converters

import com.google.gson.Gson
import io.objectbox.converter.PropertyConverter
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnitMolecule

class ProgramUnitConverter: PropertyConverter<ProgramUnit?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): ProgramUnit? {
        return databaseValue?.let {
            Gson().fromJson(it, ProgramUnit::class.java)
        }
    }

    override fun convertToDatabaseValue(entityProperty: ProgramUnit?): String? {
        return entityProperty?.let {
            Gson().toJson(it)
        }
    }
}
class NewMaterialConverter: PropertyConverter<List<ProgramUnitMolecule>?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): List<ProgramUnitMolecule>? {
        return databaseValue?.let {
            Gson().fromJson(it, Array<ProgramUnitMolecule>::class.java).toList()
        }
    }

    override fun convertToDatabaseValue(entityProperty: List<ProgramUnitMolecule>?): String? {
        return entityProperty?.let {
            Gson().toJson(it)
        }
    }
}
class ReviewConverter: PropertyConverter<List<List<ProgramUnitMolecule>>?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): List<List<ProgramUnitMolecule>>? {
        return databaseValue?.let {
            Gson().fromJson(it, Array<Array<ProgramUnitMolecule>>::class.java).map { it.toList() }
        }
    }

    override fun convertToDatabaseValue(entityProperty: List<List<ProgramUnitMolecule>>?): String? {
        return entityProperty?.let {
            Gson().toJson(it)
        }
    }
}