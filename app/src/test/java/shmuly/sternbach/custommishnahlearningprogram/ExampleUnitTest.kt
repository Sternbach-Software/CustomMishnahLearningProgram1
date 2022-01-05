package shmuly.sternbach.custommishnahlearningprogram

import org.junit.Assert.assertEquals
import org.junit.Test
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import java.time.LocalDate

//import org.junit.Test
//
//import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val variedList = mutableListOf(
        ProgramUnit("a", LocalDate.now(), true, 1, 1, 0, 1),
        ProgramUnit("b", LocalDate.now(), true, 1, 1, 1, 1),
        ProgramUnit("c", LocalDate.now(), true, 1, 1, 2, 1),
        ProgramUnit("d", LocalDate.now(), true, 1, 1, 3, 1),

        ProgramUnit("e", LocalDate.now(), true, 1, 2, 0, 1),
        ProgramUnit("f", LocalDate.now(), true, 1, 2, 1, 1),
        ProgramUnit("g", LocalDate.now(), true, 1, 2, 2, 1),
        ProgramUnit("h", LocalDate.now(), true, 1, 2, 3, 1),

        ProgramUnit("i", LocalDate.now(), true, 1, 3, 0, 1),
        ProgramUnit("j", LocalDate.now(), true, 1, 3, 1, 1),
        ProgramUnit("k", LocalDate.now(), true, 1, 3, 2, 1),

        ProgramUnit("l", LocalDate.now(), true, 1, 4, 3, 1),

        ProgramUnit("m", LocalDate.now(), true, 1, 5, 0, 1),
        ProgramUnit("n", LocalDate.now(), true, 1, 5, 1, 1),
        ProgramUnit("o", LocalDate.now(), true, 1, 5, 2, 1),
        ProgramUnit("p", LocalDate.now(), true, 1, 5, 3, 1),

        ProgramUnit("q", LocalDate.now(), true, 1, 6, 0, 1),
        ProgramUnit("r", LocalDate.now(), true, 1, 6, 1, 1),
        ProgramUnit("s", LocalDate.now(), true, 1, 6, 2, 1),
        ProgramUnit("t", LocalDate.now(), true, 1, 6, 3, 1),

        ProgramUnit("u", LocalDate.now(), true, 1, 7, 2, 1),
        ProgramUnit("v", LocalDate.now(), true, 1, 7, 3, 1),

        ProgramUnit("w", LocalDate.now(), true, 1, 8, 2, 1),
        ProgramUnit("x", LocalDate.now(), true, 1, 8, 3, 1),

        ProgramUnit("y", LocalDate.now(), true, 1, 9, 2, 1),
        ProgramUnit("z", LocalDate.now(), true, 1, 9, 3, 1),

        ProgramUnit("aa", LocalDate.now(), true, 1, 10, 2, 1),

        ProgramUnit("bb", LocalDate.now(), true, 1, 11, 3, 1),

        ProgramUnit("cc", LocalDate.now(), true, 1, 12, 3, 1),
        ProgramUnit("dd", LocalDate.now(), true, 1, 12, 3, 1),
        ProgramUnit("ee", LocalDate.now(), true, 1, 12, 3, 1),
        ProgramUnit("ff", LocalDate.now(), true, 1, 12, 3, 1),
    ) //ends in full row
    val listEndingInOne = variedList + (
        ProgramUnit("gg", LocalDate.now(), true, 1, 13, 3, 1)
    ) //ends in single row
    val listEndingInOneTwice = listEndingInOne + (
        ProgramUnit("hh", LocalDate.now(), true, 1, 14, 3, 1)
    ) //ends in single row twice in a row
    @Test
    fun getOverviewStringWorksOnOneSet() {
        val list = listOf(
            ProgramUnit("a", LocalDate.now(), true, 1, 1, 0, 1),
            ProgramUnit("b", LocalDate.now(), true, 1, 1, 1, 1),
            ProgramUnit("c", LocalDate.now(), true, 1, 1, 2, 1),
            ProgramUnit("d", LocalDate.now(), true, 1, 1, 3, 1),
        )
        val overviewString = Utils.getOverviewString(list)
        println("overviewString=$overviewString")
        assertEquals("a - d", overviewString)
    }
    @Test
    fun getOverviewStringWorksOnTwoSets() {
        val list = listOf(
            ProgramUnit("a", LocalDate.now(), true, 1, 1, 0, 1),
            ProgramUnit("b", LocalDate.now(), true, 1, 1, 1, 1),
            ProgramUnit("c", LocalDate.now(), true, 1, 1, 2, 1),
            ProgramUnit("d", LocalDate.now(), true, 1, 1, 3, 1),

            ProgramUnit("e", LocalDate.now(), true, 1, 2, 0, 1),
            ProgramUnit("f", LocalDate.now(), true, 1, 2, 1, 1),
            ProgramUnit("g", LocalDate.now(), true, 1, 2, 2, 1),
            ProgramUnit("h", LocalDate.now(), true, 1, 2, 3, 1),
        )
        val overviewString = Utils.getOverviewString(list)
        println("overviewString=$overviewString")
        assertEquals("a - d\ne - h", overviewString)
    }
    @Test
    fun getOverviewStringWorksOnThreeSets() {
        val list = listOf(
            ProgramUnit("a", LocalDate.now(), true, 1, 1, 0, 1),
            ProgramUnit("b", LocalDate.now(), true, 1, 1, 1, 1),
            ProgramUnit("c", LocalDate.now(), true, 1, 1, 2, 1),
            ProgramUnit("d", LocalDate.now(), true, 1, 1, 3, 1),

            ProgramUnit("e", LocalDate.now(), true, 1, 2, 0, 1),
            ProgramUnit("f", LocalDate.now(), true, 1, 2, 1, 1),
            ProgramUnit("g", LocalDate.now(), true, 1, 2, 2, 1),
            ProgramUnit("h", LocalDate.now(), true, 1, 2, 3, 1),

            ProgramUnit("i", LocalDate.now(), true, 1, 3, 0, 1),
            ProgramUnit("j", LocalDate.now(), true, 1, 3, 1, 1),
            ProgramUnit("k", LocalDate.now(), true, 1, 3, 2, 1),
            ProgramUnit("l", LocalDate.now(), true, 1, 3, 3, 1),
        )
        val overviewString = Utils.getOverviewString(list)
        println("overviewString=$overviewString")
        assertEquals("a - d\ne - h\ni - l", overviewString)
    }
    @Test
    fun getOverviewStringWorksOnVariesSets() {
        val overviewString = Utils.getOverviewString(variedList)
        println("overviewString=$overviewString")
        val expected =
            "a - d" +
                    "\ne - h" +
                    "\ni - k" +
                    "\nl" +
                    "\nm - p" +
                    "\nq - t" +
                    "\nu - v" +
                    "\nw - x" +
                    "\ny - z" +
                    "\naa" +
                    "\nbb" +
                    "\ncc - ff"
        assertEquals(expected, overviewString)

    }
    @Test
    fun getOverviewStringWorksOnSetEndingInOne() {
        val expected =
            "a - d" +
                    "\ne - h" +
                    "\ni - k" +
                    "\nl" +
                    "\nm - p" +
                    "\nq - t" +
                    "\nu - v" +
                    "\nw - x" +
                    "\ny - z" +
                    "\naa" +
                    "\nbb" +
                    "\ncc - ff" +
                    "\ngg"
        assertEquals(expected, Utils.getOverviewString(listEndingInOne))
    }
    @Test
    fun getOverviewStringWorksOnSetEndingInOneTwice() {
        val expected =
            "a - d" +
                    "\ne - h" +
                    "\ni - k" +
                    "\nl" +
                    "\nm - p" +
                    "\nq - t" +
                    "\nu - v" +
                    "\nw - x" +
                    "\ny - z" +
                    "\naa" +
                    "\nbb" +
                    "\ncc - ff" +
                    "\ngg" +
                    "\nhh"
        assertEquals(expected, Utils.getOverviewString(listEndingInOneTwice))
    }
}