package shmuly.sternbach.custommishnahlearningprogram.data.converters

import com.google.gson.Gson
import io.objectbox.converter.PropertyConverter
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import java.time.LocalDate

class ProgramUnitConverter: PropertyConverter< Map<LocalDate, MutablePair<ProgramUnit<String>?, MutableList<ProgramUnit<String>>>>?, String?> {
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]}...
    override fun convertToEntityProperty(databaseValue: String?): Map<LocalDate, MutablePair<ProgramUnit<String>?, MutableList<ProgramUnit<String>>>>? {
        return databaseValue
            ?.split("~")
            ?.map {
                it
                    .split("|")
                    .let {
                        Pair(
                            LocalDate.parse(it[0]),
                            it[1]
                                .split("@")
                                .let {
                                    MutablePair(
                                        ProgramUnit(
                                            it[0].split("%")
                                        ) as ProgramUnit?,
                                        if(it[1].isBlank()) mutableListOf() else
                                            it[1]
                                                .split("^")
                                                .mapTo(mutableListOf()) {
                                                    ProgramUnit(it.split("="))
                                                }
                                    )
                                }
                        )
                    }
            }
            ?.toMap()
    }

    override fun convertToDatabaseValue(entityProperty: Map<LocalDate, MutablePair<ProgramUnit<String>?, MutableList<ProgramUnit<String>>>>?): String? {
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