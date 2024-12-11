package days.aoc2024

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day11Test {
    val day = Day11()

    @Test
    fun testPartOne() {
        val input1 = "0 1 10 99 999"
        val input2 = "125 17"
        MatcherAssert.assertThat(day.calculateStonesForBlinks(input1, 1), `is`(7))
        MatcherAssert.assertThat(day.calculateStonesForBlinks(input2, 6), `is`(22))
        MatcherAssert.assertThat(day.calculateStonesForBlinks(input2, 25), `is`(55312))
    }

    @Test
    fun testPartTwo() {

    }
}