package days.aco2023

import days.aoc2023.Day6
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day6Test {
    val day = Day6()
    val input = """
Time:      7  15   30
Distance:  9  40  200
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(288))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(71503))
    }
}