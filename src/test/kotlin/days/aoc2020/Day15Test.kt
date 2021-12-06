package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day15Test {

    private val day = Day15()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(436))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(175594))
    }
}
