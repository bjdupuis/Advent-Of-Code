package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day14Test {
    val day = Day14()
    val input = """
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateAmountOfSandSettled(input), `is`(24))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateSandUntilBlocked(input), `is`(93))
    }
}