package shmuly.sternbach.custommishnahlearningprogram.randomtests

import kotlin.math.floor
import kotlin.math.truncate

fun main() {

    fun tableSizeFor(cap: Int): Int {
        var n = cap - 1
        n = n or (n ushr 1)
        n = n or (n ushr 2)
        n = n or (n ushr 4)
        n = n or (n ushr 8)
        n = n or (n ushr 16)
        return if (n < 0) 1 else {
            val i = 1 shl 30
            println("1 shl 30 = $i")
            if (n >= i) i else n + 1
        }
    }

    println("tableSizeFor(4_192)=${tableSizeFor(4_192)}")

    fun fractionOf1(double: Double) = double % 1.0// ("0." + double.toString().substringAfter('.')).toDouble()
    fun fractionOf2(double: Double) = double - floor(double)// ("0." + double.toString().substringAfter('.')).toDouble()
    fun fractionOf3(double: Double) = double - truncate(double)// ("0." + double.toString().substringAfter('.')).toDouble()
    fun fractionOf4(double: Double) = ("0." + double.toString().substringAfter('.')).toDouble()
    val double = 2.123_456_789
    val time1 = HashSet<Long>(1_000_000)
    val time2 = HashSet<Long>(1_000_000)
    val time3 = HashSet<Long>(1_000_000)
    var fraction = 0.0
    repeat(1) {
        val double1 = double * it
        var start1 = System.nanoTime()

        fraction = fractionOf2(double1)
        time2.add(System.nanoTime() - start1)

        start1 = System.nanoTime()

        fraction = fractionOf1(double1)
        time1.add(System.nanoTime() - start1)

        start1 = System.nanoTime()

        fraction = fractionOf4(double1)
        time3.add(System.nanoTime() - start1)
    }
    println(fraction)
    println(time1.average())
    println(time2.average())
    println(time3.average())
    println(fractionOf1(double))
    println(fractionOf2(double))
    println(fractionOf3(double))
    println(fractionOf2(1.0) == 0.0)
}