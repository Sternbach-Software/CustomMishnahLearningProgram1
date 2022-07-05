package shmuly.sternbach.custommishnahlearningprogram.logic

import shmuly.sternbach.custommishnahlearningprogram.data.CompletionStatus
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import java.time.LocalDate
import java.time.Period
import kotlin.math.roundToInt

class LearningProgramScheduleMakerByEndDate {

    data class ProgramMetadata(
        val numDays: Int,
        val sizeOfProgram: Int,
        val frequencyMapOfNumDaysToHowManyUnitsPerDay: Map<Int, Int>,
        val averageUnitsPerDay: Int
    )

    data class MutableReference<T>(var data: T?)

    fun generateProgram(
        material: List<String>,
        programID: Int,
        endDate: LocalDate,
        metadata: MutableList<ProgramMetadata>,
        startDate: LocalDate = LocalDate.now(),
        interval: Period = Period.of(0, 0, 1),
    ): List<ProgramUnit> {

        val numDays = startDate.until(endDate.plusDays(1)/*plus 1 because this is exclusive*/)
            .let { it.days + (it.months * 30) + (it.years * 365) } //TODO should 1 day be added to fix the exclusivity, or should 1 interval be added?
        if (material.size >= numDays) { //at least one per day
            val numUnitsPerDay = mutableListOf<Int>()
            val perDay =
                material.size / numDays //e.g. 100 units in 50 days is 2/day, 100 / 51 = 1.96078431372549
            val doubleDivision = material.size / numDays.toDouble()
            val remainder = fractionOf(doubleDivision)
//        println("Num days: ${numDays}, perDay = $perDay, doubleDivision=$doubleDivision, remainder=$remainder")
            return if (remainder == 0.0) { //divides evenly
                getListWhenDividesEvenly(material, perDay, startDate, programID, interval)
            } else {

                val list = getListBasedOnInconsistencyPolicy(
                    InconsistencyPolicy.ADD_INCONSISTENCIES_WHEN_NECESSARY,
                    material,
                    remainder,
                    perDay,
                    startDate,
                    programID,
                    numUnitsPerDay,
                    interval
                )
                metadata.add(
                    ProgramMetadata(
                        numDays,
                        list.size,
                        numUnitsPerDay.toFrequencyMap(),
                        runCatching { numUnitsPerDay.average().roundToInt() }.getOrDefault(0)
                    )
                )
                list
            }
        } else { //there aren't even enough to do 1 per day, space them out evenly
            //edge cases: less spaces than buckets, more spaces than buckets
            //3 units, 5 days, l-l-l
            //5 units, 9 days, l-l-l-l-l
            //5 units, 10 days l-l--l-l-l // when there is 1 or 2 left, go to the middle and put it on either sides of the pivot (if size is odd, pivot will be length 1. otherwise, length 2?)
            //5 units, 15 days l----l----l---l----l // when there is 1 or 2 left, go to the middle and put it on either sides of the pivot (if size is odd, pivot will be length 1. otherwise, length 2?)
            TODO()
        }
    }

    private fun getListBasedOnInconsistencyPolicy(
        inconsistencyPolicy: InconsistencyPolicy,
        material: List<String>,
        remainder: Double,
        perDay: Int,
        startDate: LocalDate,
        programID: Int,
        numUnitsPerDay: MutableList<Int>,
        interval: Period
    ) = when (inconsistencyPolicy) {
        InconsistencyPolicy.ADD_INCONSISTENCIES_TO_BEGINNING -> {
            TODO()
        }
        InconsistencyPolicy.ADD_INCONSISTENCIES_TO_END -> {
            TODO()
        }
        InconsistencyPolicy.ADD_INCONSISTENCIES_EVENLY -> {
            TODO()
        }
        InconsistencyPolicy.ADD_INCONSISTENCIES_WHEN_NECESSARY -> getListAccumulatingRemainder(
            material,
            remainder,
            perDay,
            startDate,
            programID,
            numUnitsPerDay,
            interval
        )
    }

    private fun getListAccumulatingRemainder(
        material: List<String>,
        remainder: Double,
        perDay: Int,
        today: LocalDate,
        programID: Int,
        numUnitsPerDay: MutableList<Int>,
        interval: Period
    ): MutableList<ProgramUnit> {
        var today1 = today
        var accumulatedRemainder = 0.0
        val list = mutableListOf<ProgramUnit>()
        val stack = material.toMutableList()
        //            println("Material: $material")
        var groupNumber = 0
        while (stack.isNotEmpty()) {
            //                println("Starting loop again. accumulatedRemainder=$accumulatedRemainder")
            accumulatedRemainder += remainder
            //                println("Setting accumulatedRemainder to $accumulatedRemainder")
            var numToTake = perDay
            if (accumulatedRemainder > 1) {
                numToTake += accumulatedRemainder.toInt()
                accumulatedRemainder =
                    fractionOf(accumulatedRemainder)
                //                    println("Num †o take: $numToTake, accumulatedRemainder=$accumulatedRemainder")
            }
            repeat(minOf(numToTake, stack.size)) { index ->
                list.add(
                    ProgramUnit(
                        stack.removeFirst(),
                        today1,
                        false,
                        programID,
                        groupNumber,
                        index,
                        CompletionStatus.TODO
                    )
                )
            }
            groupNumber++
            numUnitsPerDay.add(numToTake)
            today1 = today1.plus(interval)
        }
        return list
    }

    private fun getListWhenDividesEvenly(
        material: List<String>,
        perDay: Int,
        startDate: LocalDate,
        programID: Int,
        interval: Period
    ): List<ProgramUnit> {
        var today = startDate
        return material
            .windowed(perDay, perDay)
            .flatMapIndexed { groupNumber: Int, list: List<String> ->
                list
                    .mapIndexed { index, s ->
                        ProgramUnit(
                            s,
                            today,
                            false,
                            programID,
                            groupNumber,
                            index,
                            CompletionStatus.TODO
                        )
                    }
                    .also { today = today.plus(interval) }
            }
    }

fun <T> Iterable<T>.toFrequencyMap(): Map<T, Int> {
    val frequencies: MutableMap<T, Int> = mutableMapOf()
    this.forEach { frequencies[it] = frequencies.getOrDefault(it, 0) + 1 }
    return frequencies
}

private fun fractionOf(double: Double) =
    ("0." + double.toString().substringAfter('.')).toDouble()
}