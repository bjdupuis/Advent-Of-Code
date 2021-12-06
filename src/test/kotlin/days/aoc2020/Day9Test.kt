package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day9Test {

    private val day = Day9()

    @Test
    fun testPartOne() {
        assertThat(day.findFirstFailure(preambleSize = 5), `is`(127))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.findContiguousSetFor(day.findFirstFailure(preambleSize = 5)), `is`(62))
    }
}
