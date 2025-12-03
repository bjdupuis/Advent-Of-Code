package days.aoc2025

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day3Test {
    val day = Day3()
    val input = """
987654321111111
811111111111119
234234234234278
818181911112111
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(357))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(3121910778619L))
    }
}