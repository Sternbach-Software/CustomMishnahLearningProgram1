package shmuly.sternbach.custommishnahlearningprogram.data.converters

import io.objectbox.converter.PropertyConverter
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.units.ProgramUnitMaterial
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProgramConverter: PropertyConverter< Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>?, String?> {
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]}...
    override fun convertToEntityProperty(databaseValue: String?): Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>? {
        return try {
            databaseValue
                ?.split("~")
                ?.map {
                    it
                        .split("|")
                        .let {
                            Pair(
                                LocalDate.parse(it[0], DateTimeFormatter.ISO_LOCAL_DATE),
                                it[1]
                                    .split("@")
                                    .let {
                                        MutablePair(
                                            ProgramUnitMaterial(
                                                it[0].split("%")
                                            ) as ProgramUnitMaterial?,
                                            if (it[1].isBlank()) mutableListOf() else
                                                it[1]
                                                    .split("^")
                                                    .mapTo(mutableListOf()) {
                                                        ProgramUnitMaterial(it.split("="))
                                                    }
                                        )
                                    }
                            )
                        }
                }
                ?.toMap()
        } catch(t: Throwable) {
            Timber.e(t)
            ld("Value couldn't parse: $databaseValue")
            null
        }
    }

    override fun convertToDatabaseValue(entityProperty: Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>?): String? {
        //      12-34-5678
        //             |
        //      abc
        //              %
        //      abc
        //              %
        //      abc
        //      @
        //              abc
        //                      =
        //              abc
        //                      =
        //              abc
        //              ^
        //              abc
        //                      =
        //              abc
        //                      =
        //              abc
        // ~
        //      12-34-5678
        //             |
        //      abc
        //              %
        //      abc
        //              %
        //      abc
        //      @
        //              abc
        //                      =
        //              abc
        //                      =
        //              abc
        //              ^
        //              abc
        //                      =
        //              abc
        //                      =
        //              abc
        return entityProperty?.let {
            it
                .toList()
                .joinToString("~") {
                    it.first.toString() +
                            "|" +
                            it.second.let { it1 ->
                                it1
                                    .first
                                    ?.listOfMaterials
                                    ?.joinToString("%") +
                                        "@" +
                                        it1
                                            .second
                                            .joinToString("^") {
                                                it
                                                    .listOfMaterials
                                                    .joinToString("=")
                                            }
                            }
                }
//            ) //entityProperty?.listOfMaterials?.joinToString("~") }
        }
    }
}