package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day16Test {

    private val day = Day16()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(71))
    }

    @Test
    fun testPartTwo() {
        // empty
    }
}
