package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day22Test {

    private val day = Day22()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(306))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(291))
    }
}
