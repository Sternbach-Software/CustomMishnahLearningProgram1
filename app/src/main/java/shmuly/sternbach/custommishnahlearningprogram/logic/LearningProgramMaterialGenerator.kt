package shmuly.sternbach.custommishnahlearningprogram

import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.CompletionStatus
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import shmuly.sternbach.custommishnahlearningprogram.logic.Mishnayos
//import shmuly.sternbach.custommishnahlearningprogram.activities.toDateMap
//import shmuly.sternbach.custommishnahlearningprogram.data.units.ProgramUnitMaterial
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureNanoTime

val DATE_PATTERN = "EEE MMMM dd uuuu"
val LD_FOMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)
fun formatLocalDate(ld: LocalDate): String {
    return LD_FOMATTER.format(ld)
}

val inHebrew = false
fun Int.toHebrew() = if (!inHebrew) this else when (this) {
    1 -> "א"
    2 -> "ב"
    3 -> "ג"
    4 -> "ד"
    5 -> "ה"
    6 -> "ו"
    7 -> "ז"
    8 -> "ח"
    9 -> "ט"
    10 -> "י"
    11 -> "יא"
    12 -> "יב"
    13 -> "יג"
    14 -> "יד"
    15 -> "טו"
    16 -> "טז"
    17 -> "יז"
    18 -> "יח"
    19 -> "יט"
    20 -> "כ"
    21 -> "כא"
    22 -> "כב"
    23 -> "כג"
    else -> TODO("This=$this")
}
//class CreatorShmuly {

/*
* 1/28/2019
* 1-28-2019
* 1/28/19
* 1-28-19
* January 28, 2019
* Jan 28, 2019
* January 28 '19
* Jan 28 '19
* January 28
* Jan 28
* Wed 1/28/2019
* Wed 1-28-2019
* Wed January 28
* Wed Jan 28
* Wed January 28 2019
* Wed Jan 28 2019
* Wednesday, January 28
* Wednesday, January 28, 2019
* Wed
* Wednesday
* */
abstract class LearningProgramMaterialGenerator {

    abstract val programName: String
    abstract val chapterNames: List<String>
    abstract val chapterLengths: List<List<Int>>
    abstract var numReviewsPerUnit: Int
    abstract val reviewIntervals: MutableList<Int> //num days after new material to review
    //val sedarim = listOf("Zeraim", "Moed", "Nashim", "Nezikin", "Kodashim", "Taharos")
//        val totalNumberOfUnits: Int by lazy { chapterLengths.sumBy { it.size } }

    /**
     * If program is mishnayos, returns ["Berachos, 1:1", "Berachos, 1:2", ...]
     * */
//        open val allUnits: List<String> by lazy { listMappedToUnits(chapterNames, chapterLengths) }
    open val listOfPrograms by lazy { //e.g. peah-bikkurim, brachos, moed-taharos...
        mutableListOf<List<String>>().apply {
//                add(
//                    listMappedToUnits(chapterNames, chapterLengths) //equivalent to allUnits, replace with sublist to create peah-bikkurim, brachos, moed-taharos, etc.
//                )
        }
    }


    fun getAllUnits() = listMappedToUnits(chapterNames, chapterLengths)

    fun listMappedToUnits(
        listOfNames: List<String>,
        listOfListsOfLengths: List<List<Int>>
    ): MutableList<String> {
        val finalList: MutableList<String> = mutableListOf()
        for (index in listOfNames.indices) {
            val chapterName = listOfNames[index]
            val listOfLengths = listOfListsOfLengths[index]
            for (index1 in listOfLengths.indices) {
                for (chapterUnit in 1..listOfLengths[index1])
                    finalList.add("$chapterName ${(index1 + 1).toHebrew()}:${chapterUnit.toHebrew()}") //Berachos 1:1, Berachos 1:2, ...
            }
        }
        return finalList
    }

    /**
     * @param intervalToLearnNewMaterial duration that each unit lasts for (e.g. Period.of(day=1) means that a new unit is learned every day)
     * @return Pair<List<new material to date learned>, List<reviews to date-to-review>>["Berachos 1:1 - Berachos 1:4" to 01/01/2020...]
     * */
    fun generateProgram(
        startDate: LocalDate,
        intervalToLearnNewMaterial: Period,
        numUnitsPerInterval: Int,
        programID: Int
    ): Pair<List<ProgramUnit>, List<ProgramUnit>> {
        val listOfNewMaterial = mutableListOf<ProgramUnit>()
        val listOfReviews = mutableListOf<ProgramUnit>()
        var currentDate = startDate
        ld("List of programs: $listOfPrograms")
        val listOfMaterialStrings = listOfPrograms.flatten()
        val listOfUnits = listOfMaterialStrings.windowed(numUnitsPerInterval, numUnitsPerInterval, true)
        for (index in listOfUnits.indices) {
            populateNewMaterialAndReviews(
                listOfUnits[index],
                listOfNewMaterial,
                currentDate,
                listOfReviews,
                programID,
                index
            )
            currentDate = currentDate.plus(intervalToLearnNewMaterial)
        }
        ld("List of new material: ${listOfNewMaterial.take(30)}")
        ld("List of reviews: ${listOfReviews.take(30)}")
        return listOfNewMaterial to listOfReviews
    }

    private fun populateNewMaterialAndReviews(
        units: List<String>,
        listOfNewMaterial: MutableList<ProgramUnit>,
        currentDate: LocalDate,
        listOfReviews: MutableList<ProgramUnit>,
        programID: Int,
        group: Int
    ) {
        for(index in units.indices) {
            val programUnit = ProgramUnit(
                units[index],
                currentDate,
                false/*first do new material*/,
                programID,
                group,
                index,
                CompletionStatus.NONE
            )
            listOfNewMaterial.add(programUnit)
            populateListOfReviews(listOfReviews, programUnit, currentDate)
        }
    }

    private fun populateListOfReviews(
        listOfReviews: MutableList<ProgramUnit>,
        intervaledUnitMaterial: ProgramUnit,
        currentDate: LocalDate
    ) {
        for (reviewInterval in reviewIntervals) {
            listOfReviews.add(
                intervaledUnitMaterial.copy(
                    isReview = true,
                    date = currentDate.plus(
                        Period.of(
                            0,
                            0,
                            reviewInterval
                        )
                    ),
                    uuid = UUID.randomUUID()
                )
            )
        }
    }
}
//}

fun main() {
    val x = measureNanoTime {
        val mishnayos = Mishnayos()
//    println(x.allUnits)
        mishnayos.listOfPrograms.clear()
        mishnayos.listOfPrograms.apply {
            val startIndex = mishnayos.chapterNames.indexOf("Pe'ah")
            val endIndex =
                mishnayos.chapterNames.indexOf("Bikkurim") + 1 //exclusive when used as last index and inclusive when used as first endex, so save myself the math by doing it here
            add(
                mishnayos.listMappedToUnits(
                    mishnayos.chapterNames.subList(startIndex, endIndex),
                    mishnayos.chapterLengths.subList(startIndex, endIndex)
                )
            )
            add(
                mishnayos.listMappedToUnits(
                    mishnayos.chapterNames.subList(0, 1),
                    mishnayos.chapterLengths.subList(0, 1)
                ) //berachos
            )
            add(
                mishnayos.listMappedToUnits(
                    mishnayos.chapterNames.subList(endIndex, mishnayos.chapterNames.size),
                    mishnayos.chapterLengths.subList(endIndex, mishnayos.chapterLengths.size)
                )
            )
        }
        val program = mishnayos.generateProgram(
            startDate = LocalDate.now(),
            intervalToLearnNewMaterial = Period.of(0, 0, 1),
            4,
            1,
        )
//        File("Program.txt").apply {
//            if (exists()) delete()
//            createNewFile()
//            appendText("New material: ${program.first}\n")
//            appendText(
//                "Reviews: ${
//                    program.second.sortedBy { it.second }
//                        .mapIndexed { index, pair -> "$index~$pair" }
//                }"
//            )
//        }
        println(LocalDate.parse("2010-10-10").plus(Period.of(1, 1, 1)))
    }
    println(x)
}
/*

fun stringToMap(databaseValue: String?): Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>? {
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]} ~
    //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]}...
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
//                ProgramUnitMaterial(it)
        }
        ?.toMap()
}

fun mapToString(entityProperty: Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>?): String? {
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
            .joinToString("~") {//...~...~...
                it.first.toString() +
                        "|" +
                        it.second.let { it1 ->  //dd-dd-dddd|(abc%abc%abc)@{[abc=abc=abc]^[abc=abc-abc]^...}
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
}*/
