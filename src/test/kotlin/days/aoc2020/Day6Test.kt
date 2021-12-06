package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day6Test {

    private val day = Day6()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(11))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(6))
    }
}
