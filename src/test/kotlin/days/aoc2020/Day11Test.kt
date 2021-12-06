package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day11Test {

    private val day = Day11()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(37))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(26))
    }
}
