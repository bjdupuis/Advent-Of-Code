package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day10Test {

    private val day = Day10()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(220))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(19208L))
    }
}
