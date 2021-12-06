package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day18Test {

    private val day = Day18()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`((26 + 437 + 12240 + 13632).toLong()))
    }

    @Test
    fun testPartTwo() {
        val input =
"""
1 + (2 * 3) + (4 * (5 + 6))
2 * 3 + (4 * 5)
5 + (8 * 3 + 9 + 3 * 4 * 3)
5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))
((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2
""".trimIndent().lines()

        assertThat(day.calculatePart2(input), `is`((51 + 46 + 1445 + 669060 + 23340).toLong()))
    }
}
