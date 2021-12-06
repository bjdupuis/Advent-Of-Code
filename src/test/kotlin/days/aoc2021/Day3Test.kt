package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day3Test {
    val day = Day3()
    val input =
        """
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""".trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateGammaAndEpsilon(input), Is.`is`(Pair(22,9)))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateGenAndScrubberRating(input), Is.`is`(Pair(23,10)))
    }
}