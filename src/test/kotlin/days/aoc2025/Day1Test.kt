package days.aoc2025

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day1Test {
    val day = Day1()
    val input = """
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(3))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(6))
    }
}