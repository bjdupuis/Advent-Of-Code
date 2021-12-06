package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day4Test {

    private val day = Day4()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(8))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(4))
    }
}
