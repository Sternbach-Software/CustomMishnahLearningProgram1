package shmuly.sternbach.custommishnahlearningprogram.logic

/**
 * Policy for how to create program when units dont dvide evenly into days.
 * For example, 24 units in 7 days. 24 / 7 = 3R3. The extra 3 can be added at sseveral places
 * in the program to finish by the deadline.
 * Such as by adding them to the beginning, as in: 4-4-4-3-3-3-3. Or at the end: 3-3-3-3-4-4-4
 * */
enum class InconsistencyPolicy {

    /*4-4-4 - 3-3-3-3*/
    ADD_INCONSISTENCIES_TO_BEGINNING,

    /*3-3-3-3 - 4-4-4*/
    ADD_INCONSISTENCIES_TO_END,

    /*3-4-3-4-3-4-3*/
    ADD_INCONSISTENCIES_EVENLY,

    /*3-3-4 - 3-3-4 - 3-3-4*/
    ADD_INCONSISTENCIES_WHEN_NECESSARY,
}