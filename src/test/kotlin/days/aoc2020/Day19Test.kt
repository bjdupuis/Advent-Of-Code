package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day19Test {

    private val day = Day19()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(3))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(12))
    }
}
