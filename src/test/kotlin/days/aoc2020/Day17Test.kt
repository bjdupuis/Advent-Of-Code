package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day17Test {

    private val day = Day17()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(112))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(848))
    }
}
