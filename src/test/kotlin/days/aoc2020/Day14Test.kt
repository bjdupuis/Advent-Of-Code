package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day14Test {

    private val day = Day14()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(165L))
    }

    @Test
    fun testPartTwo() {
        val inputList =
"""
mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1
""".trimIndent().lines()

        assertThat(day.calculatePartTwo(inputList), `is`(208L))
    }
}
