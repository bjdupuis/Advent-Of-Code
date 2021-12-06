package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day23Test {

    private val day = Day23()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(67384529))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(149245887792L))
    }
}
