package shmuly.sternbach.custommishnahlearningprogram.randomtests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONStringer
import shmuly.sternbach.custommishnahlearningprogram.logic.LearningProgramScheduleMakerByEndDate
import timber.log.Timber
import java.io.File
import java.security.SecureRandom
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.random.asKotlinRandom
import kotlin.system.measureNanoTime

class Foo(val a: Char, val b: List<Foo>)

val recursiveList = listOf(
    Foo(
        'a',
        listOf(
            Foo('b', listOf(Foo('c', listOf()))),
            Foo('d', listOf(Foo('e', listOf(Foo('f', listOf())))))
        )
    )
)
val stringBuilder = StringBuilder()
fun main() {
    //3 hours, 2200
    val numEntries = 4_192
    val material = List(numEntries) { List(it + 1) { it.toString() } }
    val now = LocalDate.now()
    val days = List(numEntries) { now.plusDays(it.toLong()) }
//    val material = MutableList(1000) { (it + 1).toString() }/*Mishnayos().getAllUnits()*/
//    val twoPerDay = LearningProgramScheduleMakerByEndDate().generateProgram(list, 0, LocalDate.now().plusDays(50),)
//    println("Two per day: ${twoPerDay.joinToString("\n", "\n")}")
    val listOfMetadata = Collections.synchronizedList(ArrayList<LearningProgramScheduleMakerByEndDate.ProgramMetadata>(
        numEntries
    ))
    val maker = LearningProgramScheduleMakerByEndDate()
    val scope = CoroutineScope(SupervisorJob())
    val interval = Period.of(0, 0, 1)
    val atomicInt = AtomicInteger(0)
    val total = numEntries * numEntries
    val intRange = 0 until numEntries
    for (numUnits in intRange) {
        for (numDaysAfterToday in intRange) {
            scope.launch(Dispatchers.Default) {
                try {
                    maker.generateProgram(
                        material[numUnits],
                        0,
                        days[numDaysAfterToday],
                        listOfMetadata,
                        now,
                        interval
                    )
                    println("Finished computation number ${atomicInt.incrementAndGet()}/$total")
                } catch (t: Throwable) {
                    println("Failed: generateProgram(material[$numUnits], 0, days[$numDaysAfterToday], listOfMetadata, now, interval)")
                    t.printStackTrace()
                }
            }
        }
    }
    val jsonStringer = JSONStringer().array()
    for (metadata in listOfMetadata) {
        jsonStringer
            .`object`()
            .key("numDays")
            .value(metadata.numDays)
            .key("numUnits")
            .value(metadata.numUnits)
            .apply {
                val jobject = JSONObject()
                for (entry in metadata.freqMap) {
                    jobject.put(entry.key.toString(), entry.value)
                }
                key("freqMap")
                value(jobject)
            }
            .endObject()
    }
    jsonStringer.endArray()
    File("results.txt").writeText(jsonStringer.toString())
    println(listOfMetadata)
    /*val twoPerDayWithRemainder = maker.generateProgram(
        material,
        0,
        now.plusDays(49),
        metadata
    )
    println("Two per day with remainder: ${twoPerDayWithRemainder.joinToString("\n", "\n")}")
*///    println("Two per day with remainder: $twoPerDayWithRemainder")
/*val strings = mutableListOf<String>()
    for(i in 1..100_000) strings.add(filterBenchmark())
    populateStringBuilder(strings)
    println(stringBuilder)*/
//    println()
}

val secureRandom = SecureRandom().asKotlinRandom()//for benchmarking filter functions
var bool = false
fun filterBenchmark(): String {
    val list = mutableListOf<() -> Pair<Int, Long>>()
//    println("SpeakerInListOfSpeakers freq map: ${originalList.map{(it as ShiurFullPage).speaker}.toFrequencyMap().toList().sortedBy { it.second }}")
//    println("Category freq map: ${originalList.map{(it as ShiurFullPage).category}.toFrequencyMap().toList().sortedBy { it.second }}")
    list.add({
        0 to measureNanoTime {
            bool = recursiveList.recursiveFind({ it.a == 'z' }) { it.b } == null &&
                    recursiveList.recursiveFind({ it.a == 'a' }) { it.b } != null &&
                    recursiveList.recursiveFind({ it.a == 'f' }) { it.b } != null
        }
    })
    list.add({
        1 to measureNanoTime {
            bool = recursiveList.recursiveFind1({ it.a == 'z' }) { it.b } == null &&
                    recursiveList.recursiveFind1({ it.a == 'a' }) { it.b } != null &&
                    recursiveList.recursiveFind1({ it.a == 'f' }) { it.b } != null
        }
    })
    val listOfResults = mutableListOf<Pair<Int, Long>>()
    list.shuffled(secureRandom).forEach {
        Timber.d("New algorithm processed: ${list.indexOf(it)}")
        listOfResults.add(it())
    }
    listOfResults.sortBy { it.second }
    val listOfResultsString = listOfResults.joinToString(prefix = "~", postfix = "\n")
//    File("benchmark_results.txt").appendText(listOfResultsString)
    return listOfResultsString
}

fun <T> List<T>.recursiveFind1(predicate: (T) -> Boolean, recursiveSelector: (T) -> List<T>): T? {
    if (isEmpty()) return null
    for (element in this) {
        if (predicate(element)) return element
        val children = recursiveSelector(element)
        return if (children.isEmpty()) continue
        else children.recursiveFind1(predicate, recursiveSelector) ?: continue
    }
    return null
}

fun <E> List<E>.recursiveFind(predicate: (E) -> Boolean, recursiveSelector: (E) -> List<E>): E? {
    var find = find(predicate)//check this layer
    if (find != null) return find
    for (element in this) {
        find = recursiveSelector(element).recursiveFind(
            predicate,
            recursiveSelector
        )//check the next layer
        if (find != null) return find
    }
    return null
}

fun populateStringBuilder(lines1: List<String>) {

    fun toLookBehindMatchAhead(behind: String, match: String, ahead: String): Regex {
        return "(?<=$behind)$match(?=$ahead)".toRegex()
    }

    fun <T> Iterable<T>.toFrequencyMap(): Map<T, Int> {
        val frequencies: MutableMap<T, Int> = mutableMapOf()
        this.forEach { frequencies[it] = frequencies.getOrDefault(it, 0) + 1 }
        return frequencies
    }

    /**
     * Turns a number into an ordinal string representation (e.g. 4.toOrdinalNumber()== "4th"
     * */
    fun Int.toOrdinalNumber(): String {
        val suffixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (this % 100) {
            11, 12, 13 -> "${this}th"
            else -> this.toString() + suffixes[this % 10]
        }
    }

    val maxes = mutableListOf<Double>()
    val mins = mutableListOf<Double>()
    val sizes = mutableSetOf<String>()
    //        val nanoSecondsRegex = "(?<=, )\\d+".toRegex()
    //        val algorithmNumRegex = "(?<=\\()\\d".toRegex()

    data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

    val minMaxAverageOfPositions = mutableListOf<Quad<Int, Double?, Double?, Double?>>()
    val minMaxAverageOfAlgorithm = mutableListOf<Quad<Int, Double?, Double?, Double?>>()
    val listOfPositionToTime = mutableListOf<Pair<Int, Long>>()
    val listOfAlgorithmToTime = mutableListOf<Pair<Int, Long>>()
    val listOfListsOfAlgorithmToTime = mutableListOf<List<Pair<Int, Long>>>()
    val mapOfAlgorithmToTimes = mutableMapOf<Int, MutableList<Long>>()
    val linesBySize = mutableMapOf<String, MutableList<String>>()
    lines1.forEach {
        val size = it.substringBefore("~")
        if (size in sizes) linesBySize[size]!!.add(it)
        else {
            linesBySize[size] = mutableListOf<String>().apply { add(it) }
            sizes.add(size)
        }
    }
    Timber.d("Sizes: $sizes")
    for ((size, lines) in linesBySize) {
        maxes.clear()
        mins.clear()
        minMaxAverageOfPositions.clear()
        minMaxAverageOfAlgorithm.clear()
        listOfPositionToTime.clear()
        listOfAlgorithmToTime.clear()
        listOfListsOfAlgorithmToTime.clear()
        mapOfAlgorithmToTimes.clear()
        val diffs = lines.map {
            val listOfAlgorithmToTimeOneIteration =
                toLookBehindMatchAhead("\\(", "\\d, \\d+", "\\)").findAll(it)
                    .map { it1 ->
                        it1.value.split(", ")
                            .let { it2 -> Pair(it2[0].toInt(), it2[1].toLong()) }
                    }.toList()
            listOfAlgorithmToTime.addAll(listOfAlgorithmToTimeOneIteration)
            listOfListsOfAlgorithmToTime.add(listOfAlgorithmToTimeOneIteration)
            val nanoseconds = listOfAlgorithmToTimeOneIteration.map { it.second }
            val min = nanoseconds.minOrNull()!!.div(1_000_000_000.00)
            val max = nanoseconds.maxOrNull()!!.div(1_000_000_000.00)
            maxes.add(max)
            mins.add(min)
            listOfPositionToTime.addAll(nanoseconds.mapIndexed { index, l -> Pair(index, l) })
            Pair(
                max - min,
                max / min
            ) //best possible gain from using the worst method to the best and the factor to which it is better
        }
        val mapOfPositionToTimes = mutableMapOf<Int, MutableList<Long>>()
        listOfAlgorithmToTime.map { it.first }
            .forEach { mapOfAlgorithmToTimes[it] = mutableListOf() }
        listOfPositionToTime.map { it.first }
            .forEach { mapOfPositionToTimes[it] = mutableListOf() }
        listOfPositionToTime.forEach { mapOfPositionToTimes[it.first]?.add(it.second) }
        listOfAlgorithmToTime.forEach { mapOfAlgorithmToTimes[it.first]?.add(it.second) }
        minMaxAverageOfPositions.addAll(mapOfPositionToTimes.map {
            Quad(
                it.key,
                it.value.minOrNull()?.div(1_000_000_000.00),
                it.value.maxOrNull()?.div(1_000_000_000.00),
                it.value.average().div(1_000_000_000.00)
            )
        })
        minMaxAverageOfPositions.sortBy { it.a }
        minMaxAverageOfAlgorithm.addAll(mapOfAlgorithmToTimes.map {
            Quad(
                it.key,
                it.value.minOrNull()?.div(1_000_000_000.00),
                it.value.maxOrNull()?.div(1_000_000_000.00),
                it.value.average().div(1_000_000_000.00)
            )
        })
        minMaxAverageOfAlgorithm.sortBy { it.a }
        //        val formatter = NumberFormat.getNumberInstance()
        fun Iterable<Pair<Double, Double>>.maxOrNull(chooser: (Pair<Double, Double>) -> Double): Double? {
            val iterator = iterator()
            if (!iterator.hasNext()) return null
            var max = chooser(iterator.next())
            while (iterator.hasNext()) {
                val e = chooser(iterator.next())
                max = maxOf(max, e)
            }
            return max
        }

        fun Iterable<Pair<Double, Double>>.minOrNull(chooser: (Pair<Double, Double>) -> Double): Double? {
            val iterator = iterator()
            if (!iterator.hasNext()) return null
            var min = chooser(iterator.next())
            while (iterator.hasNext()) {
                val e = chooser(iterator.next())
                min = minOf(min, e)
            }
            return min
        }
        stringBuilder.append(lines.joinToString("\n").let {
            "Size of list: $size \n" +
                    "Biggest gain in using a better algorithm across one iteration of all algorithms (local):  ${diffs.maxOrNull { it.first }!!} seconds, by a factor of ${diffs.maxOrNull { it.second }!!}; Smallest gain: ${diffs.minOrNull { it.first }!!}, by a factor of ${diffs.minOrNull { it.second }!!}\n" +
                    "Biggest gain in using a better algorithm across all iterations of all algorithms(global): ${maxes.maxOrNull()!! - mins.minOrNull()!!}, Smallest gain: ${maxes.minOrNull()!! - mins.maxOrNull()!!}\n\n" +

                    "Worst case: ${maxes.maxOrNull()}\n" +
                    "Best case: ${mins.minOrNull()}\n" +
                    "Average best case: ${mins.average()}\n" +
                    "Average worst case: ${maxes.average()}\n\n" +


                    (listOfListsOfAlgorithmToTime.first().indices).fold("") { acc, i ->
                        acc + "${(i + 1).toOrdinalNumber()} place frequency:  ${
                            listOfListsOfAlgorithmToTime.map { it[i].first }
                                .toFrequencyMap().toList()
                                .sortedByDescending { it.second }
                        }\n"
                    } + "\n" +

                    minMaxAverageOfPositions
                        .indices
                        .fold("") { acc: String, position: Int ->
                            acc + "${(position + 1).toOrdinalNumber()} place Min-Max-Average:  ${minMaxAverageOfPositions[position].let { "min=${it.b}, max=${it.c}, avg=${it.d}" }}\n"
                        } + "\n" +

                    minMaxAverageOfAlgorithm
                        .indices
                        .fold("") { acc: String, position: Int ->
                            acc + "Algorithm $position Min-Max-Average:  ${minMaxAverageOfAlgorithm[position].let { "min=${it.b}, max=${it.c}, avg=${it.d}" }}\n"
                        } + "\n"
        })
    }

}