package shmuly.sternbach.custommishnahlearningprogram

import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.CompletionStatus
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
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
abstract class LearningProgramMaker {

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

open class Mishnayos : LearningProgramMaker() {
    val hebrewList = listOf(
        "ברכות",
        "פאה",
        "דמאי",
        "כלאים",
        "שביעית",
        "תרומות",
        "מעשרות",
        "חלה",
        "ערלה",
        "ביכורים",
        "שבת",
        "עירובין",
        "פסחים",
        "שקלים",
        "יומא",
        "סוכה",
        "ביצה",
        "תענית",
        "מגילה",
        "חגיגה",
        "יבמות",
        "כתובות",
        "נדרים",
        "נזיר",
        "סוטה",
        "גיטין",
        "קידושין",
        "סנהדרין",
        "מכות",
        "שבועות",
        "עדיות",
        "אבות",
        "הוריות",
        "זבחים",
        "מנחות",
        "חולין",
        "בכורות",
        "ערכין",
        "תמורה",
        "כריתות",
        "מעילה",
        "תמיד",
        "מדות",
        "קינים",
        "כלים",
        "אהלות",
        "נגעים",
        "פרה",
        "טהרות",
        "מקואות",
        "נדה",
        "מכשירין",
        "זבים",
        "ידים",
        "עוקצים",
    )
    val englishList = listOf( //Masechtos
        "Berachos",
        "Pe'ah",
        "Demai",
        "Kil'aim",
        "Shevi'is",
        "Terumos",
        "Ma'aseros",
        "Ma'aser Sheni",
        "Chalah",
        "Orlah",
        "Bikkurim",
        "Shabbos",
        "Eruvin",
        "Pesachim",
        "Shekalim",
        "Yoma",
        "Sukkah",
        "Beitzah",
        "Rosh HaShanah",
        "Ta'anis",
        "Megilah",
        "Moed Kattan",
        "Chaggigah",
        "Yevamos",
        "Kesubos",
        "Nedarim",
        "Nazir",
        "Sotah",
        "Gittin",
        "Kidushin",
        "Bava Kamma",
        "Bava Metzia",
        "Bava Basra",
        "Sanhedrin",
        "Makkos",
        "Shevuos",
        "Eduyos",
        "Avodah Zara",
        "Avos",
        "Horayos",
        "Zevachim",
        "Menachos",
        "Chullin",
        "Bechoros",
        "Arachin",
        "Temurah",
        "Kerisos",
        "Me'ilah",
        "Tamid",
        "Middos",
        "Kinnim",
        "Keilim",
        "Ohalos",
        "Negaim",
        "Parah",
        "Taharos",
        "Mikva'os",
        "Niddah",
        "Machshirin",
        "Zavim",
        "Tevul Yom",
        "Yadayim",
        "Uktzim"
    )
    override val chapterLengths: List<List<Int>> = listOf( //Perakim
        listOf(5, 8, 6, 7, 5, 8, 5, 8, 5),
        listOf(6, 8, 8, 11, 8, 11, 8, 9),
        listOf(4, 5, 6, 7, 11, 12, 8),
        listOf(9, 11, 7, 9, 8, 9, 8, 6, 10),
        listOf(8, 10, 10, 10, 9, 6, 7, 11, 9, 9),
        listOf(10, 6, 9, 13, 9, 6, 7, 12, 7, 12, 10),
        listOf(8, 8, 10, 6, 8),
        listOf(7, 10, 13, 12, 15),
        listOf(9, 8, 10, 11),
        listOf(9, 17, 9),
        listOf(11, 11, 12, 5),
        listOf(11, 7, 6, 2, 4, 10, 4, 7, 7, 6, 6, 6, 7, 4, 3, 8, 8, 3, 6, 5, 3, 6, 5, 5),
        listOf(10, 6, 9, 11, 9, 10, 11, 11, 4, 15),
        listOf(7, 8, 8, 9, 10, 6, 13, 8, 11, 9),
        listOf(7, 5, 4, 9, 6, 6, 7, 8),
        listOf(8, 7, 11, 6, 7, 8, 5, 9),
        listOf(11, 9, 15, 10, 8),
        listOf(10, 10, 8, 7, 7),
        listOf(9, 8, 9, 9),
        listOf(7, 10, 9, 8),
        listOf(11, 6, 6, 10),
        listOf(10, 5, 9),
        listOf(8, 7, 8),
        listOf(4, 10, 10, 13, 6, 6, 6, 6, 6, 9, 7, 6, 13, 9, 10, 7),
        listOf(10, 10, 9, 12, 9, 7, 10, 8, 9, 6, 6, 4, 11),
        listOf(4, 5, 11, 8, 6, 10, 9, 7, 10, 8, 12),
        listOf(7, 10, 7, 7, 7, 11, 4, 2, 5),
        listOf(9, 6, 8, 5, 5, 4, 8, 7, 15),
        listOf(6, 7, 8, 9, 9, 7, 9, 10, 10),
        listOf(10, 10, 13, 14),
        listOf(4, 6, 11, 9, 7, 6, 7, 7, 12, 10),
        listOf(8, 11, 12, 12, 11, 8, 11, 9, 13, 6),
        listOf(6, 14, 8, 9, 11, 8, 4, 8, 10, 8),
        listOf(6, 5, 8, 5, 5, 6, 11, 7, 6, 6, 6),
        listOf(10, 8, 16),
        listOf(7, 5, 11, 13, 5, 7, 8, 6),
        listOf(14, 10, 12, 12, 7, 3, 9, 7),
        listOf(9, 7, 10, 12, 12),
        listOf(18, 16, 18, 22, 23, 11),
        listOf(5, 7, 8),
        listOf(4, 5, 6, 6, 8, 7, 6, 12, 7, 8, 8, 6, 8, 10),
        listOf(4, 5, 7, 5, 9, 7, 6, 7, 9, 9, 9, 5, 11),
        listOf(7, 10, 7, 7, 5, 7, 6, 6, 8, 4, 2, 5),
        listOf(7, 9, 4, 10, 6, 12, 7, 10, 8),
        listOf(4, 6, 5, 4, 6, 5, 5, 7, 8),
        listOf(6, 3, 5, 4, 6, 5, 6),
        listOf(7, 6, 10, 3, 8, 9),
        listOf(4, 9, 8, 6, 5, 6),
        listOf(4, 5, 9, 3, 6, 4, 3),
        listOf(9, 6, 8, 7, 4),
        listOf(4, 5, 6),
        listOf(
            9,
            8,
            8,
            4,
            11,
            4,
            6,
            11,
            8,
            8,
            9,
            8,
            8,
            8,
            6,
            8,
            17,
            9,
            10,
            7,
            3,
            10,
            5,
            17,
            9,
            9,
            12,
            10,
            8,
            4
        ),
        listOf(8, 7, 7, 3, 7, 7, 6, 6, 16, 7, 9, 8, 6, 7, 10, 5, 5, 10),
        listOf(6, 5, 8, 11, 5, 8, 5, 10, 3, 10, 12, 7, 12, 13),
        listOf(4, 5, 11, 4, 9, 5, 12, 11, 9, 6, 9, 11),
        listOf(9, 8, 8, 13, 9, 10, 9, 9, 9, 8),
        listOf(8, 10, 4, 5, 6, 11, 7, 5, 7, 8),
        listOf(7, 7, 7, 7, 9, 14, 5, 4, 11, 8),
        listOf(6, 11, 8, 10, 11, 8),
        listOf(6, 4, 3, 7, 12),
        listOf(5, 8, 6, 7),
        listOf(5, 4, 5, 8),
        listOf(6, 10, 12)
    )
    override var numReviewsPerUnit: Int = 6
    override val reviewIntervals: MutableList<Int> = mutableListOf()
    override val programName: String = "Mishnayos"
    override val chapterNames: List<String> = if (inHebrew) hebrewList else englishList
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
        /*val program = mishnayos.generateProgram(
            startDate = LocalDate.now(),
            intervalToLearnNewMaterial = Period.of(0, 0, 1),
            4
        ).toDateMap()
        val a = mapToString(program)
        val b = stringToMap(a)
        println("Program:       ${program.toList().take(2)}")
        println("String to map: ${b!!.toList().take(2)}")
        println("Map to string: ${a!!.take(200)}")
        println("program correctness: ${b == program}")*/
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
